document.addEventListener("DOMContentLoaded", () => {
  const transactionForm = document.getElementById("transaction-form");
  const myTransactionList = document.getElementById("my-transaction-list");
  const jwt = parseJwt(_getIdToken());
  const email = jwt.email;
  const username = jwt["cognito:username"];
  const server = "https://8yc6yop4e7.execute-api.us-east-1.amazonaws.com/Beta";
  const toggleMyTransactionsBtn = document.getElementById(
    "toggle-my-transactions-btn"
  );

  let showMyTransactions = true;

  fetchTransactions();
  transactionForm.addEventListener("submit", handleSubmitForm);

  toggleMyTransactionsBtn?.addEventListener("click", () => {
    showMyTransactions = !showMyTransactions;
    updateMyTransactionsVisibility();
  });

  function handleSubmitForm(e) {
    e.preventDefault();

    const formData = new FormData(transactionForm);

    const transaction = {
      email: email,
      username: username,
      sourceAccount: formData.get("sourceAccount"),
      destinationAccount: formData.get("destinationAccount"),
      amount: parseFloat(formData.get("amount")),
      date: formData.get("date"),
    };

    postTransaction(transaction)
      .then((response) => {
        if (response.ok) {
          fetchTransactions();
          transactionForm.reset();
          mostrarPopup();
        } else {
          alert("Failed to add transaction");
        }
      })
      .catch((error) => console.error("Error:", error));
  }

  function postTransaction(transaction) {
    return fetch(server + "/secured/transactions", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        access_token: _getIdToken(),
      },
      body: JSON.stringify(transaction),
    });
  }

  function fetchTransactions() {
    fetch(server + "/secured/transactions", {
      method: "GET",
      headers: {
        access_token: _getIdToken(),
      },
    })
      .then((response) => response.json())
      .then(displayTransactions)
      .catch((error) => console.error("Error:", error));
  }

  function displayTransactions(transactions) {
    myTransactionList.innerHTML = "";
    transactions.forEach((transaction) => {
      const li = document.createElement("li");
      li.innerHTML = `
            <div class="history-transaction-container">
                ${transaction.date}
                <div>
                    <p> Source Account: ${transaction.sourceAccount} </p>
                    <p> Destination Account:${transaction.destinationAccount} </p>
                    <p> Amount: $${transaction.amount}  </p>
                    <p> Was it fraudulent?: ${transaction.isFraudulent} </p>
                </div>
            </div>
            `;
      if (transaction.username == username) {
        myTransactionList.insertBefore(li, myTransactionList.firstChild);
      }
    });
  }

  function updateMyTransactionsVisibility() {
    if (showMyTransactions) {
      myTransactionList.style.display = "";
    } else {
      myTransactionList.style.display = "none";
    }
  }

  function _getIdToken() {
    const urlCompleta = location.href;
    const urlParts = urlCompleta.split("#");
    if (urlParts.length < 2) {
      throw new Error("Invalid URL format, missing hash fragment");
    }

    const fragmentParams = new URLSearchParams(urlParts[1]);

    const idToken = fragmentParams.get("id_token");

    if (!idToken) {
      throw new Error("id_token not found in URL fragment");
    }
    return idToken;
  }

  function _getAccessToken() {
    const urlCompleta = location.href;
    const urlParts = urlCompleta.split("#");
    if (urlParts.length < 2) {
      throw new Error("Invalid URL format, missing hash fragment");
    }

    const fragmentParams = new URLSearchParams(urlParts[1]);

    const idToken = fragmentParams.get("access_token");

    if (!idToken) {
      throw new Error("id_token not found in URL fragment");
    }
    return idToken;
  }
});

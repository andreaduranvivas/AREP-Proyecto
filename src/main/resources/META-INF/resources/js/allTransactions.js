document.addEventListener('DOMContentLoaded', () => {
    const transactionList = document.getElementById('transaction-list');
    const server = "http://localhost:8080";
    fetchTransactions();

    function fetchTransactions() {
        fetch('/secured/transactions')
          .then(response => response.json())
          .then(transactions => {
               displayTransactions(transactions);
           })
          .catch(error => console.error('Error:', error));
    }

    function displayTransactions(transactions) {
        transactionList.innerHTML = "";
        transactions.forEach((transaction) => {
          const li = document.createElement("li");
          li.innerHTML = `
                <div class="history-transaction-container">
                    ${transaction.date}
                    <div>
                        <p> Source Account: ${transaction.sourceAccount} </p>
                        <p> Destination Account:${transaction.destinationAccount} </p>
                        <p> Amount: $${transaction.amount}  </p>
                        <p> Was Fraudulent: ${transaction.fraudulent} </p>
                    </div>
                </div>
                `;
          transactionList.insertBefore(li, transactionList.firstChild);
        });
      }
});

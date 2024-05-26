document.addEventListener('DOMContentLoaded', () => {
    const transactionList = document.getElementById('transaction-list');

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
        transactionList.innerHTML = '';
        transactions.forEach(transaction => {
            const li = document.createElement('li');
            li.textContent = `${transaction.date} - ${transaction.sourceAccount} -> ${transaction.destinationAccount}: $${transaction.amount} (Fraudulent: ${transaction.isFraudulent})`;
            transactionList.appendChild(li);
        });
    }
});

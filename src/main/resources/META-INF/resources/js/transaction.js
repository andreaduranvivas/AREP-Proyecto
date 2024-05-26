document.addEventListener('DOMContentLoaded', () => {
    const transactionForm = document.getElementById('transaction-form');
    const transactionList = document.getElementById('transaction-list');

    fetchTransactions();
    transactionForm.addEventListener('submit', handleSubmitForm);

    function handleSubmitForm(e) {
        e.preventDefault();

        const formData = new FormData(transactionForm);
        const transaction = {
            sourceAccount: formData.get('sourceAccount'),
            destinationAccount: formData.get('destinationAccount'),
            amount: parseFloat(formData.get('amount')),
            date: formData.get('date'),
            isFraudulent: formData.get('isFraudulent') === 'on'
        };


        postTransaction(transaction)
            .then(response => {
                if (response.ok) {
                    fetchTransactions();
                    transactionForm.reset();
                } else {
                    alert('Failed to add transaction');
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function postTransaction(transaction) {
        return fetch('/transactions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(transaction)
        });
    }

    function fetchTransactions() {
        fetch('/transactions')
            .then(response => response.json())
            .then(displayTransactions)
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

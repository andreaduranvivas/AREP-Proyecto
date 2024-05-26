document.addEventListener('DOMContentLoaded', () => {
    const transactionForm = document.getElementById('transaction-form');
    const transactionList = document.getElementById('transaction-list');
    const toggleTransactionsBtn = document.getElementById('toggle-transactions-btn');

    let showTransactions = true;

    fetchTransactions();
    transactionForm.addEventListener('submit', handleSubmitForm);

    toggleTransactionsBtn?.addEventListener('click', () => {
        showTransactions = !showTransactions;
        updateTransactionsVisibility();
    });

    function handleSubmitForm(e) {
        e.preventDefault();

        const formData = new FormData(transactionForm);
        const isFraudulent = document.getElementById('isFraudulent').checked;

        const transaction = {
            sourceAccount: formData.get('sourceAccount'),
            destinationAccount: formData.get('destinationAccount'),
            amount: parseFloat(formData.get('amount')),
            date: formData.get('date'),
            isFraudulent: isFraudulent
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
        return fetch('/secured/transactions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(transaction)
        });
    }

    function fetchTransactions() {
        fetch('/secured/transactions')
            .then(response => response.json())
            .then(displayTransactions)
            .catch(error => console.error('Error:', error));
    }

    function displayTransactions(transactions) {
        transactionList.innerHTML = '';
        transactions.forEach(transaction => {
            console.log(transaction);
            const li = document.createElement('li');
            li.textContent = `${transaction.date} - ${transaction.sourceAccount} -> ${transaction.destinationAccount}: $${transaction.amount} (Fraudulent: ${transaction.fraudulent})`;
            transactionList.appendChild(li);
        });
    }

    function updateTransactionsVisibility() {
        if (showTransactions) {
            transactionList.style.display = '';
        } else {
            transactionList.style.display = 'none';
        }
    }
});

// Store expenses in an array
let expenses = [];

// DOM Elements
const categorySelect = document.getElementById('category');
const amountInput = document.getElementById('amount');
const addExpenseButton = document.getElementById('addExpense');
const calculateButton = document.getElementById('calculate');
const expensesList = document.getElementById('expensesList');
const errorDiv = document.getElementById('error');
const totalExpensesElement = document.getElementById('totalExpenses');
const averageExpensesElement = document.getElementById('averageExpenses');
const topExpensesElement = document.getElementById('topExpenses');

// Format number as currency
const formatCurrency = (number) => {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(number);
};

// Show error message
const showError = (message) => {
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
    setTimeout(() => {
        errorDiv.style.display = 'none';
    }, 3000);
};

// Validate input
const validateInput = (category, amount) => {
    if (!category) {
        showError('Please select a category');
        return false;
    }
    if (!amount || amount <= 0) {
        showError('Please enter a valid amount');
        return false;
    }
    return true;
};

// Add new expense
const addExpense = () => {
    const category = categorySelect.value;
    const amount = parseFloat(amountInput.value);

    if (!validateInput(category, amount)) {
        return;
    }

    const expense = {
        id: Date.now(),
        category,
        amount,
        date: new Date().toLocaleDateString()
    };

    expenses.push(expense);
    updateExpensesList();
    resetForm();
};

// Remove expense
const removeExpense = (id) => {
    expenses = expenses.filter(expense => expense.id !== id);
    updateExpensesList();
    calculateExpenses();
};

// Reset form
const resetForm = () => {
    categorySelect.value = '';
    amountInput.value = '';
};

// Update expenses list
const updateExpensesList = () => {
    expensesList.innerHTML = '';
    expenses.forEach(expense => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${expense.category}</td>
            <td>${formatCurrency(expense.amount)}</td>
            <td>${expense.date}</td>
            <td>
                <button class="remove-btn" onclick="removeExpense(${expense.id})">
                    Remove
                </button>
            </td>
        `;
        expensesList.appendChild(row);
    });
};

// Calculate expenses
const calculateExpenses = () => {
    // Total expenses
    const total = expenses.reduce((sum, expense) => sum + expense.amount, 0);
    totalExpensesElement.textContent = formatCurrency(total);

    // Average daily expenses
    const averageDaily = total / 30;
    averageExpensesElement.textContent = formatCurrency(averageDaily);

    // Top 3 expenses
    const top3 = [...expenses]
        .sort((a, b) => b.amount - a.amount)
        .slice(0, 3);

    topExpensesElement.innerHTML = top3
        .map(expense => `
            <li>${expense.category}: ${formatCurrency(expense.amount)}</li>
        `)
        .join('');
};

// Event listeners
addExpenseButton.addEventListener('click', addExpense);
calculateButton.addEventListener('click', calculateExpenses);

// Handle form submission with Enter key
amountInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        addExpense();
    }
});

// Initialize the application
updateExpensesList();
calculateExpenses(); 
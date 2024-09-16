const BASE_API_URL = 'http://localhost:8080/customers';

let currentPage = 0;
let totalPages = 0;

// Function to fetch data from the API with authorization header
async function fetchData(page = 0, size = 5, sortBy = 'firstName', sortDir = 'asc') {
    try {
        const token = localStorage.getItem('jwt');
        if (!token) {
            window.location.href = '../Auth/index.html';
            throw new Error('No JWT token found in local storage');
        }

        const response = await fetch(`${BASE_API_URL}/all?page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        appendCustomers(data.content); // 'content' has the actual customer data

        // Update the total pages and current page based on response
        currentPage = data.pageable.pageNumber;
        totalPages = data.totalPages;

        handlePagination(data);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

// Function to append customer data to the table
function appendCustomers(customers) {
    const tbody = document.getElementById('customers');
    tbody.innerHTML = '';

    customers.forEach(customer => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${customer.first_name}</td>
            <td>${customer.last_name}</td>
            <td>${customer.street}</td>
            <td>${customer.address}</td>
            <td>${customer.city}</td>
            <td>${customer.state}</td>
            <td>${customer.email}</td>
            <td>${customer.phone}</td>
            <td>
                <button onclick="togglePopup('edit', '${customer.uuid}')">✏️</button>
                <button onclick="deleteCustomer('${customer.uuid}')">❌</button>
            </td> <!-- Combined Edit and Delete -->
        `;
        tbody.appendChild(row);
    });
}

// Function to handle pagination controls
function handlePagination(data) {
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');

    prevBtn.disabled = data.first;  
    nextBtn.disabled = data.last;
}

function next() {
    if (currentPage < totalPages - 1) {
        currentPage++;
        fetchData(currentPage); 
    }
}

function previous() {
    if (currentPage > 0) {
        currentPage--;
        fetchData(currentPage); 
    }
}

// Fetching data when the page loads
document.addEventListener('DOMContentLoaded', () => {
    fetchData();
});

// Function to add a customer
async function addCustomer() {
    try {
        const token = localStorage.getItem('jwt');
        if (!token) {
            window.location.href = '../Auth/index.html';
            throw new Error('No JWT token found in local storage');
        }

        const response = await fetch(`${BASE_API_URL}`, {  // Update the endpoint
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                first_name: document.getElementById('newFirstName').value,
                last_name: document.getElementById('newLastName').value,
                street: document.getElementById('newStreet').value,
                address: document.getElementById('newAddress').value,
                city: document.getElementById('newCity').value,
                state: document.getElementById('newState').value,
                email: document.getElementById('newEmail').value,
                phone: document.getElementById('newPhone').value
            })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }


        const data = await response.json();
        console.log('Customer added successfully:', data);
        alert('Customer added successfully!')
        togglePopup('add'); 
        fetchData(); 
    } catch (error) {
        console.error('Error adding customer:', error);
    }
}

// Function to save the customer changes
async function saveCustomer() {
    const uuid = window.currentEditCustomerId;

    const token = localStorage.getItem('jwt');
    if (!token) {
        alert('No JWT token found in local storage');
        return;
    }

    const updatedData = {
        first_name: document.getElementById('editFirstName').value,
        last_name: document.getElementById('editLastName').value,
        street: document.getElementById('editStreet').value,
        address: document.getElementById('editAddress').value,
        city: document.getElementById('editCity').value,
        state: document.getElementById('editState').value,
        email: document.getElementById('editEmail').value,
        phone: document.getElementById('editPhone').value
    };

    try {
        const response = await fetch(`${BASE_API_URL}/${uuid}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedData)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const updatedCustomer = await response.json();
        console.log('Customer updated:', updatedCustomer);

        togglePopup('edit');

        fetchData();
    } catch (error) {
        console.error('Error updating customer:', error);
    }
}

// Function to delete a customer
async function deleteCustomer(uuid) {
    if (!confirm("Are you sure you want to delete this customer?")) {
        return;
    }

    try {
        const token = localStorage.getItem('jwt');
        if (!token) {
            throw new Error('No JWT token found in local storage');
        }

        const response = await fetch(`${BASE_API_URL}/${uuid}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const deletedCustomer = await response.json();
        console.log('Customer deleted:', deletedCustomer);

        fetchData(currentPage);
    } catch (error) {
        console.error('Error deleting customer:', error);
    }
}

// Function to apply filters (search and sort)
async function filter() {
    const searchBy = document.getElementById('searchBy').value;
    const searchTerm = document.getElementById('searchTerm').value;
    const sortBy = document.getElementById('sortBy').value;
    const sortOrder = document.getElementById('sortOrder').value;

    const url = `${BASE_API_URL}/search/${searchBy}?keyword=${searchTerm}&sortBy=${sortBy}&sortDir=${sortOrder}`;

    try {
        const token = localStorage.getItem('jwt');
        if (!token) {
            throw new Error('No JWT token found in local storage');
        }

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        appendCustomers(data.content);
        handlePagination(data);
    } catch (error) {
        console.error('Error applying filters:', error);
    }
}

// Function to logout the user
function logout() {
    localStorage.removeItem('jwt');
    window.location.href = '../Auth/index.html';
}

// Function to sync customers from the remote API to the local database
async function syncCustomers() {
    const syncStatus = document.getElementById('syncStatus');
    syncStatus.innerHTML = "Syncing customers...";

    try {
        const token = localStorage.getItem('jwt');
        if (!token) {
            throw new Error('No JWT token found in local storage');
        }

        // Make a POST request to the backend /sync endpoint
        const response = await fetch(`${BASE_API_URL}/sync`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to sync customers: ${response.status}`);
        }

        const message = await response.text();
        syncStatus.innerHTML = message; 

        fetchData(); 
    } catch (error) {
        syncStatus.innerHTML = `Error: ${error.message}`;
        console.error('Error syncing customers:', error);
    }
}

// Function to toggle the visibility of the add and edit customer popups
function togglePopup(popupType, uuid) {
    const addPopup = document.getElementById('addCustomerPopup');
    const editPopup = document.getElementById('editCustomerPopup');

    if (popupType === 'add') {
        if (addPopup.style.display === 'block') {
            addPopup.style.display = 'none';
        } else {
            addPopup.style.display = 'block'; 
            if (editPopup.style.display === 'block') {
                editPopup.style.display = 'none'; 
            }
        }
    } else if (popupType === 'edit') {
        if (editPopup.style.display === 'block') {
            editPopup.style.display = 'none'; 
        } else {
            editPopup.style.display = 'block'; 
            if (addPopup.style.display === 'block') {
                addPopup.style.display = 'none';
            }
            // Populate the form if UUID is provided
            if (uuid) {
                document.getElementById('editFirstName').value = '';
                document.getElementById('editLastName').value = '';
                document.getElementById('editStreet').value = '';
                document.getElementById('editAddress').value = '';
                document.getElementById('editCity').value = '';
                document.getElementById('editState').value = '';
                document.getElementById('editEmail').value = '';
                document.getElementById('editPhone').value = '';
                window.currentEditCustomerId = uuid; // Store the UUID for later use
            }
        }
    }
}

const loginBtn = document.getElementById('loginBtn');
const registerBtn = document.getElementById('registerBtn');
const actionLoginBtn = document.getElementById('actionLoginBtn');
const actionRegisterBtn = document.getElementById('actionRegisterBtn');
const closeModalBtn = document.getElementById('closeModalBtn');
const modal = document.getElementById('authModal');

function openModal() {
    modal.style.display = 'block';
}

function closeModal() {
    modal.style.display = 'none';
}

window.onclick = function(event) {
    if (event.target === modal) {
        closeModal();
    }
}

loginBtn.addEventListener('click', openModal);
registerBtn.addEventListener('click', openModal);
actionLoginBtn.addEventListener('click', openModal);
actionRegisterBtn.addEventListener('click', openModal);
closeModalBtn.addEventListener('click', closeModal);
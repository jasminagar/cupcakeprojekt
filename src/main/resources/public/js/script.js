const openCartButton = document.getElementById('openCart');
const closeCartButton = document.getElementById('closeCart');
const cartDrawer = document.getElementById('cartDrawer');
const cartOverlay = document.getElementById('cartOverlay');

function openCart() {
    if (!cartDrawer) return;
    document.body.classList.add('cart-open');
    cartDrawer.setAttribute('aria-hidden', 'false');
}

function closeCart() {
    if (!cartDrawer) return;
    document.body.classList.remove('cart-open');
    cartDrawer.setAttribute('aria-hidden', 'true');
}

if (openCartButton) {
    openCartButton.addEventListener('click', openCart);
}

if (closeCartButton) {
    closeCartButton.addEventListener('click', closeCart);
}

if (cartOverlay) {
    cartOverlay.addEventListener('click', closeCart);
}

document.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
        closeCart();
    }
});

function showForm(type) {
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");
    const loginTab = document.getElementById("loginTab");
    const registerTab = document.getElementById("registerTab");

    if (type === "register") {
        registerForm.classList.add("active");
        loginForm.classList.remove("active");
        registerTab.classList.add("active");
        loginTab.classList.remove("active");
    } else {
        loginForm.classList.add("active");
        registerForm.classList.remove("active");
        loginTab.classList.add("active");
        registerTab.classList.remove("active");
    }
}

const activeTab = "[[${activeTab}]]";
if (activeTab === "register") {
    showForm("register");
} else {
    showForm("login");
}

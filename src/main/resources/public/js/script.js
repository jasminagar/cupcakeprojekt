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

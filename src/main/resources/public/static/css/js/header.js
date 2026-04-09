document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("cartBtn");
    const dropdown = document.getElementById("cartDropdown");

    if (!btn || !dropdown) return;

    btn.addEventListener("click", () => {
        dropdown.classList.toggle("open");
    });

    document.addEventListener("click", (e) => {
        if (!btn.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.classList.remove("open");
        }
    });
});
const API_BASE = `${window.location.origin}/api`;

function getToken() {
  return localStorage.getItem('token');
}

function getUser() {
  const raw = localStorage.getItem('user');
  return raw ? JSON.parse(raw) : null;
}

function setAuth(token, user) {
  localStorage.setItem('token', token);
  localStorage.setItem('user', JSON.stringify(user));
}

function clearAuth() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
}

function requireAuth() {
  if (!getToken()) {
    window.location.href = 'login.html';
    return false;
  }
  return true;
}

function logout() {
  clearAuth();
  window.location.href = 'login.html';
}

async function api(path, options = {}) {
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  const token = getToken();
  if (token) headers.Authorization = `Bearer ${token}`;

  const res = await fetch(API_BASE + path, { ...options, headers });
  const text = await res.text();
  let data = null;
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = { message: text };
    }
  }

  if (!res.ok) {
    throw new Error(data?.message || res.statusText);
  }
  return data;
}

async function register(name, email, password) {
  const data = await api('/auth/register', {
    method: 'POST',
    body: JSON.stringify({ name, email, password }),
  });
  setAuth(data.token, data.user);
}

async function login(email, password) {
  const data = await api('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password }),
  });
  setAuth(data.token, data.user);
}

async function loadProducts() {
  return api('/products');
}

async function loadCart() {
  return api('/cart');
}

async function addToCart(productId, quantity = 1) {
  return api('/cart', {
    method: 'POST',
    body: JSON.stringify({ productId, quantity }),
  });
}

async function updateCartItem(id, quantity) {
  return api(`/cart/${id}`, {
    method: 'PUT',
    body: JSON.stringify({ quantity }),
  });
}

async function removeCartItem(id) {
  return api(`/cart/${id}`, { method: 'DELETE' });
}

async function checkout(shippingAddress) {
  return api('/orders/checkout', {
    method: 'POST',
    body: JSON.stringify({ shippingAddress }),
  });
}

async function loadOrders() {
  return api('/orders');
}

async function loadProfile() {
  return api('/profile');
}

async function updateProfile(payload) {
  return api('/profile', {
    method: 'PUT',
    body: JSON.stringify(payload),
  });
}

function renderNav(active) {
  const user = getUser();
  const nav = document.getElementById('mainNav');
  if (!nav) return;
  nav.innerHTML = `
    <a href="index.html" class="${active === 'home' ? 'active' : ''}">Home</a>
    <a href="products.html" class="${active === 'products' ? 'active' : ''}">Shop</a>
    <a href="cart.html" class="${active === 'cart' ? 'active' : ''}">Cart</a>
    <a href="orders.html" class="${active === 'orders' ? 'active' : ''}">Orders</a>
    <a href="profile.html" class="${active === 'profile' ? 'active' : ''}">Profile</a>
    ${user ? `<span class="nav-user">Hi, ${user.name}</span><button type="button" class="btn-link" onclick="logout()">Logout</button>` : `<a href="login.html">Login</a>`}
  `;
}

function formatMoney(n) {
  return `$${Number(n).toFixed(2)}`;
}

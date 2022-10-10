import storage from '@/utils/storage'
const TokenKey = 'vue_admin_template_token'

export function getToken() {
    return storage.getItem(TokenKey)
}

export function setToken(token) {
    return storage.setItem(TokenKey, token)
}

export function removeToken() {
    return storage.removeItem(TokenKey)
}

window.AndroidEM = {};
AndroidEM.firebaseEvent = function(event_name, key) {
    window.AndroidWebView.firebaseEvent(event_name, key);
}
function AndroidEMTest() {
    return true;
}
function FBPurchase(amount, currency) {
    AndroidEM.onPurchase(amount, currency.replace(/\s/g,''));
}
function emitFirebaseEvent(event_name, key) {
    AndroidEM.firebaseEvent(event_name, key);
}

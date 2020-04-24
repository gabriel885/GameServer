export default class {
  constructor() {
    this.listeners = []; // list to event
  }

  on(listener, callOn) {
    this.listeners.push({ listener, callOn });
  }

  off(listener) {
    for (let i = 0; i < this.listeners; i++) {
      if (this.listeners[i].listener === listener) {
        this.listeners.splice(i, 1); // remove listener
        break;
      }
    }
  }

  emit() {
    // notify players
    for (const l of this.listeners) {
      l.listener.apply(l.callOn, arguments);
    }
  }
}

import {LitElement, html} from 'lit';
import {customElement, property} from 'lit/decorators.js';

/**
 * The app root component
 * @element app-root
 */
@customElement('app-root')
export class App extends LitElement {
  @property({type: String}) name = 'World';
  
  protected render() {
    return html`
      <h1>Hello, ${this.name}!</h1>
      <input @input="${this._handleInput}"></input>
    `;
  }

  private _handleInput(e: Event) {
    this.name = (e.target as HTMLInputElement).value;
  }
}

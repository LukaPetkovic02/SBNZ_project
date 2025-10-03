import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

type Card = {
  rank: string;
  suit: string;
  src: string;
  alt: string;
};

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected title = 'frontend';

  // reactive state bound to the template
  playerCards: Card[] = [];
  cpuLeftCards: Card[] = [];
  cpuRightCards: Card[] = [];
  communityCards: Card[] = [];
  handResult: string = '';

  // suits and ranks used to generate image paths
  // ranks are short form (A,2..10,J,Q,K) because backend expects these;
  // but image files use full names (ace, jack, queen, king) so we'll map when building file paths
  private ranks = ['A','2','3','4','5','6','7','8','9','T','J','Q','K'];
  private suits = ['spades','hearts','diamonds','clubs'];

  // Shuffle & deal preflop, then call backend with player's cards
  dealPreflop(){
    const deck: Card[] = [];
    const rankToFile = (r: string) => {
      switch(r.toUpperCase()){
        case 'A': return 'ace';
        case 'J': return 'jack';
        case 'Q': return 'queen';
        case 'K': return 'king';
        case 'T': return '10';
        default: return r; // numbers (2..10)
      }
    };

    for(const r of this.ranks){
      for(const s of this.suits){
        const fileRank = rankToFile(r);
        const file = `${fileRank}_of_${s}.png`;
        deck.push({rank: r, suit: s, src: `/assets/images/cards/${file}`, alt: `${r} of ${s}`} as Card);
      }
    }

    // simple Fisher-Yates shuffle
    for(let i = deck.length -1; i>0; i--){
      const j = Math.floor(Math.random()*(i+1));
      [deck[i], deck[j]] = [deck[j], deck[i]];
    }

    // deal: player gets first two, CPUs next two each, community empty for preflop
    this.playerCards = [deck.pop()!, deck.pop()!];
    this.cpuLeftCards = [deck.pop()!, deck.pop()!];
    this.cpuRightCards = [deck.pop()!, deck.pop()!];
    this.communityCards = [];
    this.handResult = 'Checking hand...';

    // call backend with player's cards
    const mapSuit = (s: string) => {
      switch(s){
        case 'spades': return 'SPADE';
        case 'hearts': return 'HEART';
        case 'diamonds': return 'DIAMOND';
        case 'clubs': return 'CLUB';
        default: return s.toUpperCase();
      }
    };
    const mapRank = (r: string) => {
      // accept either short form (A,J,Q,K) or full name
      const up = r.toUpperCase();
      switch(up){
        case 'A':
        case 'ACE': return 'ACE';
        case 'K':
        case 'KING': return 'KING';
        case 'Q':
        case 'QUEEN': return 'QUEEN';
        case 'J':
        case 'JACK': return 'JACK';
        case 'T':
        case '10': return '10';
        default: return up;
      }
    };

    const c1 = this.playerCards[0];
    const c2 = this.playerCards[1];
    const params = new URLSearchParams({
      card1Rank: c1.rank,
      card1Suit: mapSuit(c1.suit),
      card2Rank: c2.rank,
      card2Suit: mapSuit(c2.suit),
    });

    const url = `http://localhost:8080/template-example?${params.toString()}`;
    // Use fetch so we don't have to add HttpClientModule
    fetch(url)
      .then(res => res.text())
      .then(txt => {
        // backend returns a string like: "Hand strength: 0.9 hand category: PREMIUM"
        this.handResult = txt;
      })
      .catch(err => {
        console.error('backend call failed', err);
        this.handResult = 'Error calling backend';
      });
  }
}

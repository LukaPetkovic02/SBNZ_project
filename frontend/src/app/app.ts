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

  // chips
  playerChips: number = 2000;
  cpuLeftChips: number = 2000;
  cpuRightChips: number = 2000;

  // CEP simulation state
  cpuLeftProfile = { profileType: 'UNKNOWN', confidence: 0.0 };
  cpuRightProfile = { profileType: 'UNKNOWN', confidence: 0.0 };
  cepLogs: string[] = [];

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

  // helper sleep
  private sleep(ms: number){ return new Promise(resolve => setTimeout(resolve, ms)); }

  private pushLog(msg: string){
    this.cepLogs.unshift(`${new Date().toLocaleTimeString()} — ${msg}`);
    // keep reasonable length
    if(this.cepLogs.length > 200) this.cepLogs.pop();
  }

  // Simulate the CEP sequence described on the backend
  async simulateCep(){
    // reset
    this.cpuLeftProfile = { profileType: 'UNKNOWN', confidence: 0.0 };
    this.cpuRightProfile = { profileType: 'UNKNOWN', confidence: 0.0 };
    this.cepLogs = [];
    // reset chips to starting stack
    this.playerChips = 2000;
    this.cpuLeftChips = 2000;
    this.cpuRightChips = 2000;

  let pot = 0;

    try{
      const backendUrl = 'http://localhost:8080/cep/bluff-detect';
      this.pushLog(`Calling backend...`);
      fetch(backendUrl, { method: 'GET', mode: 'cors' })
        .then(res => {
          if(res.ok){
            this.pushLog('Backend CEP call completed successfully');
          } else {
            this.pushLog(`Backend CEP responded with status: ${res.status}`);
          }
        })
        .catch(err => {
          console.error('Backend CEP call failed', err);
        });
    } catch (e: any){
      this.pushLog('Failed to start backend CEP call: ' + e?.message);
    }

    // follow the console sequence from backend
  this.pushLog('Player 2 raised 200 with weak hand (strength=0.25) → BluffEvent created');
  this.cpuRightChips -= 200; pot += 200; this.playerChips += 200; this.pushLog(`Player 2 bets 200 → P2 chips=${this.cpuRightChips} pot=${pot} YOU chips=${this.playerChips}`);
  await this.sleep(500);

  this.pushLog('Player 1 raised 200 with weak hand (strength=0.2) → BluffEvent created');
  this.cpuLeftChips -= 200; pot += 200; this.playerChips += 200; this.pushLog(`Player 1 bets 200 → P1 chips=${this.cpuLeftChips} pot=${pot} YOU chips=${this.playerChips}`);
  await this.sleep(500);

  this.pushLog('Player 1 raised 200 with weak hand (strength=0.2) → BluffEvent created');
  this.cpuLeftChips -= 200; pot += 200; this.playerChips += 200; this.pushLog(`Player 1 bets 200 → P1 chips=${this.cpuLeftChips} pot=${pot} YOU chips=${this.playerChips}`);
  await this.sleep(500);

  this.pushLog('Player 2 raised 200 with weak hand (strength=0.25) → BluffEvent created');
  this.cpuRightChips -= 200; pot += 200; this.playerChips += 200; this.pushLog(`Player 2 bets 200 → P2 chips=${this.cpuRightChips} pot=${pot} YOU chips=${this.playerChips}`);
  await this.sleep(500);

  this.pushLog('Player 1 raised 200 with weak hand (strength=0.2) → BluffEvent created');
  this.cpuLeftChips -= 200; pot += 200; this.playerChips += 200; this.pushLog(`Player 1 bets 200 → P1 chips=${this.cpuLeftChips} pot=${pot} YOU chips=${this.playerChips}`);
  await this.sleep(500);

  this.pushLog('Player 1 classified as frequent bluffer');
  this.cpuLeftProfile.profileType = 'FREQUENT_BLUFFER';
  this.cpuLeftProfile.confidence = 0.50;
  await this.sleep(600);

  this.pushLog('Player 2 raised 200 with weak hand (strength=0.25) → BluffEvent created');
  this.cpuRightChips -= 200; pot += 200; this.playerChips+=200; this.pushLog(`Player 2 bets 200 → P2 chips=${this.cpuRightChips} pot=${pot}`);
  await this.sleep(500);

  this.pushLog('Player 2 classified as frequent bluffer');
  this.cpuRightProfile.profileType = 'FREQUENT_BLUFFER';
  this.cpuRightProfile.confidence = 0.50;
  await this.sleep(600);

    // Player 1 continues to bluff -> confidence increases
    const increases = [0.60, 0.70, 0.80, 0.90];
    for(const conf of increases){
      this.pushLog('Player 1 raised 100 with weak hand (strength=0.2) → BluffEvent created');
  this.cpuLeftChips -= 100; pot += 100; this.playerChips += 100; this.pushLog(`Player 1 bets 100 → P1 chips=${this.cpuLeftChips} pot=${pot} YOU chips=${this.playerChips}`);
      await this.sleep(300);
      this.cpuLeftProfile.confidence = conf;
      this.pushLog(`Player 1 confidence increased to ${conf.toFixed(2)}`);
      await this.sleep(400);
    }

    this.pushLog('Player 1 is now TILTED');
    this.cpuLeftProfile.profileType = 'TILTED';
    await this.sleep(300);

  this.pushLog('Player 1 raised 100 with weak hand (strength=0.2) → BluffEvent created');
  this.cpuLeftChips -= 100; pot += 100; this.playerChips += 100; this.pushLog(`Player 1 bets 100 → P1 chips=${this.cpuLeftChips} pot=${pot} YOU chips=${this.playerChips}`);
  await this.sleep(400);

    // Player 2 confidence decreases (simulate losses / folding)
    const decreases = [0.40, 0.30, 0.20];
    for(const conf of decreases){
      // simulate losses reducing chips
  this.cpuRightChips -= 100; pot += 100; this.playerChips += 100; this.pushLog(`Player 2 loses 100 → P2 chips=${this.cpuRightChips} pot=${pot} YOU chips=${this.playerChips}`);
      this.cpuRightProfile.confidence = conf;
      this.pushLog(`Player 2 confidence decreased to ${conf.toFixed(2)}`);
      await this.sleep(350);
    }

    this.pushLog('Player 2 profile is STABLE');
    this.cpuRightProfile.profileType = 'STABLE';
    // backend final shows 0.50 as final confidence for player2
    this.cpuRightProfile.confidence = 0.50;
    await this.sleep(200);

  //this.pushLog(`Final pot=${pot}. YOU chips=${this.playerChips}`);
  this.pushLog(`Player 1 profile after: ${this.cpuLeftProfile.profileType} confidence: ${this.cpuLeftProfile.confidence.toFixed(2)}`);
  this.pushLog(`Player 2 profile after: ${this.cpuRightProfile.profileType} confidence: ${this.cpuRightProfile.confidence.toFixed(2)}`);
  }
}

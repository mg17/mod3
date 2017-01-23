/*

  CST 338: Software Design
  Module 3 - Decks of Cards

  @author Brian Brooks <bbrooks@csumb.edu>
  @author Michael Goss <mgoss@csumb.edu>
  @author Shawn Wills <swills@csumb.edu>

 */

import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class Module3
{

    public static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args)
    {

        int numOfHands = 0;
        while(numOfHands == 0 || numOfHands < 0 || numOfHands > 10) {
          System.out.print("How many hands? (1-10, please): ");
          numOfHands = keyboard.nextInt();
        }

        /* Create array of Hands and initialize */
        Hand[] hands = new Hand[numOfHands];
        for(int i = 0; i < numOfHands; i++) {
            hands[i] = new Hand();
        }

        /* We'll use a single deck */
        int numOfDecks = 1;
        Deck deck = new Deck(numOfDecks);

        /* Keep track of how many cards we have remaining,
           since Deck does not provide a method to obtain
           this number. We want to ensure we're not dealing
           more cards than we have. */
        int numOfCardsLeft = numOfDecks * 52;

        /* Using the aforementioned number, we begin dealing
           cards -- one card to each hand, then loop. */
        while(numOfCardsLeft > 0) {
            for(int i = 0; i < numOfHands; i++) {
                /* The while condition may no longer be true at this
                   stage, so we need to double check. This is because
                   the while condition only gets checked every numOfHands
                   times. */
                if(numOfCardsLeft != 0) {
                    hands[i].takeCard(deck.dealCard());
                    numOfCardsLeft--;
                }
            }
        }

        /* Display the contents of each of our hands. */
        for(int i = 0; i < numOfHands; i++) {
            System.out.println("Hand = " + hands[i] + "\n");
        }

        System.out.println("===== Shuffling Deck =====");

        /* Maintain the same number of hands, but reset them all. */
        for(int i = 0; i < numOfHands; i++) {
            hands[i].resetHand();
        }

        /* Reinitalize our decks, don't reallocate the master pack,
           then shuffle. */
        deck.init(numOfDecks);
        deck.shuffle();

        /* Keep track of how many cards we have remaining,
           since Deck does not provide a method to obtain
           this number. We want to ensure we're not dealing
           more cards than we have. */
        numOfCardsLeft = numOfDecks * 52;

        /* Using the aforementioned number, we begin dealing
           cards -- one card to each hand, then loop. */
        while(numOfCardsLeft > 0) {
            for(int i = 0; i < numOfHands; i++) {
                /* The while condition may no longer be true at this
                   stage, so we need to double check. This is because
                   the while condition only gets checked every numOfHands
                   times. */
                if(numOfCardsLeft != 0) {
                    hands[i].takeCard(deck.dealCard());
                    numOfCardsLeft--;
                }
            }
        }

        /* Display the contents of each of our hands. */
        for(int i = 0; i < numOfHands; i++) {
            System.out.println("Hand = " + hands[i] + "\n");
        }

        /*Card c = new Card();
        System.out.println(c);
        Card c2 = new Card('9', Card.Suit.diamonds);
        System.out.println(c2);
        c2.set('0', Card.Suit.hearts);
        System.out.println(c2);
        Deck.allocateMasterPack();
        Deck deck = new Deck(3);
        deck.init(3);
        deck.init(3);
        deck.shuffle();*/
    }

}

class Card
{

    public static enum Suit { clubs, diamonds, hearts, spades };
    public static Character[] validValues = { 'A', '2', '3', '4', '5', '6', '7',
            '8', '9', 'T', 'J', 'Q', 'K'};

    private char value;
    private Suit suit;
    private boolean errorFlag;


    /**
     * Creates a card with default parameters
     */
    public Card()
    {
        this('A', Suit.spades);
    }

    /**
     * Creates a card with the given value and suit.
     */
    public Card(char value, Suit suit)
    {
        this.set(value, suit);
    }

    /**
     * Outputs the card in a human-readable format
     * @return String representation of the card
     */
    public String toString()
    {
        return (this.errorFlag) ? "[Invalid]" :
            Character.toString(this.value) + " of " + this.suit;
    }

    /**
     * Sets the value of the card to the given value and suit,
     * after ensuring that the parameters are valid.
     * @return A boolean indicating success or failure
     */
    public boolean set(char value, Suit suit)
    {
        if (this.isValid(value, suit)) {
            this.value = value;
            this.suit = suit;
            this.errorFlag = false;
        } else
            this.errorFlag = true;
        return !this.errorFlag;
    }

    /**
     * Compares the value and suit of two cards to determine if they are equal
     * @param card Card to compare
     * @return A boolean indicating equality
     */
    public boolean equals(Card card)
    {
        return card.getValue() == this.getValue() &&
            card.getSuit() == this.getSuit();
    }

    /**
     * Returns the value of the card
     * @return The value of the card
     */
    public char getValue()
    {
        return this.value;
    }

    /**
     * Returns the suit of the card
     * @return The suit of the card
     */
    public Suit getSuit()
    {
        return this.suit;
    }

    /**
     * Returns the error state of the card
     * @return A boolean indicating if the card is in an invalid state
     */
    public boolean getError()
    {
        return this.errorFlag;
    }

    /**
     * Determines if the card's value and suit are valid
     * @param value The card's value
     * @param suit The card's suit
     * @return A boolean indicating if the card's parameters are valid.
     */
    private boolean isValid(char value, Suit suit)
    {
        return Arrays.asList(Card.validValues).contains(new Character(value));
    }

}

class Hand
{

    public static int MAX_CARDS = 50;

    private Card[] myCards = new Card[MAX_CARDS];
    private int numCards;

    /**
     * Initialize an empty hand
     */
    public Hand()
    {
        this.numCards = 0;
    }

    /**
     * Clears the hand by setting all array values to null
     */
    public void resetHand()
    {
        for(int i = 0; i < MAX_CARDS; i++) {
            myCards[i] = null;
        }
        this.numCards = 0;
    }

    /**
     * Takes a card and adds it to the hand
     * @return A boolean indicating success or failure
     */
    public boolean takeCard(Card card)
    {
        if(this.numCards <= MAX_CARDS) {
            myCards[this.numCards] = card;
            this.numCards++;
            return true;
        }
        return false;
    }

    /**
     * Removes the card from the hand and adjusts the numCards value
     * @return The card that was removed from the hand
     */
    public Card playCard()
    {
        Card card = this.myCards[numCards - 1];
        this.myCards[numCards - 1] = null;
        numCards--;
        return card;
    }

    /**
     * Outputs the hand in a human-readable format
     * @return String representation of the hand
     */
    public String toString()
    {
        String output = "";
        for(int i = 0; i < this.numCards; i++) {
            output = output + this.inspectCard(i) + ", ";
        }
        return output;
    }

    /**
     * Returns the card at the given index
     * @param k Index of the card
     * @return Card object, Returns a card with errorFlag = true if k is bad.
     */
    public Card inspectCard(int k)
    {
        if (k >= 0 && k < this.numCards)
            return this.myCards[k];
        else
            return new Card('0', Card.Suit.clubs);
    }

    /**
     * Returns the number of cards in the hand, NOT the length of the array.
     * @return Number of cards in the hand
     */
    public int getNumCards()
    {
        return this.numCards;
    }

}

class Deck
{

    public static int MAX_CARDS = 312;

    private static Card[] masterPack = new Card[52];

    private Card[] cards;
    private int topCard;
    private int numPacks;

    /**
     * Initialize a deck with a single pack
     */
    Deck()
    {
        this(1);
    }

    /**
     * Ensures that the number of packs wouldn't result in MAX_CARDS
     * being exceeded before creating the deck.
     */
    Deck(int numPacks)
    {
        Deck.allocateMasterPack();
        if(numPacks > MAX_CARDS/52)
            this.numPacks = 6;
        this.numPacks = numPacks;

        this.init(this.numPacks);
    }

    /**
     * Initializes the deck with the specified number of packs.
     * This number gets multiplied by 52 to create the array.
     * @param numPacks The number of packs to generate.
     */
    public void init(int numPacks)
    {
        this.cards = new Card[this.numPacks * 52];
        for(int i = 0; i < this.numPacks; i++) {
            for(int j = 0; j < 52; j++) {
                cards[i + j] = masterPack[j];
            }
        }
        this.topCard = ((this.numPacks * 52) - 1);
        return;
    }

    /**
     * Shuffles the deck using Fisher-Yates algorithm.
     * See "C: How to Program" by Deitel and Deitel or
     * "The Art of Computer Programming" by Knuth.
     */
    public void shuffle()
    {
        Random rand = new Random();
        for(int i = this.numPacks * 52 - 1; i > 0; i--) {
            int r = rand.nextInt(i + 1);
            if (r != i) {
                Card tmpCard = this.cards[r];
                this.cards[r] = this.cards[i];
                this.cards[i] = tmpCard;
            }
        }
    }

    /**
     * Deals the top card in the deck, then sets its value to null
     * and adjusts the topCard variable.
     * @return The top card object
     */
    public Card dealCard()
    {
        Card c = this.cards[this.topCard];
        this.cards[this.topCard] = null;
        this.topCard--;
        return c;
    }

    /**
     * Generates the initial 52 cards without shuffling. Only gets
     * allocated a single time during this program's runtime.
     */
    public static void allocateMasterPack()
    {
        if(masterPack[0] != null)
            return;

        int curCard = 0;
        for(Card.Suit suit : Card.Suit.values()) {
            for(int j = 0; j < Card.validValues.length; j++) {
                masterPack[curCard] = new Card(Card.validValues[j], suit);
                curCard++;
            }
        }
        return;
    }

    /**
     * Returns the top card in the deck
     * @return Top card in the deck
     */
    public int getTopCard()
    {
        return this.topCard;
    }


    /**
     * Returns the number of packs
     * @return Number of packs
     */
    public int getNumPacks()
    {
        return this.numPacks;
    }

    /**
     * Returns the card at the given index
     * @param k Index of the card
     * @return Card object, Returns a card with errorFlag = true if k is bad.
     */
    public Card inspectCard(int k)
    {
        if (k >=0 && k <= getTopCard())
            return this.cards[k];
        else
            return new Card('0', Card.Suit.clubs);
    }

}

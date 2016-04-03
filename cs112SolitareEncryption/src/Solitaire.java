
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		//did private methods to make neater and see where code fails
		jokerApriv();
		
	} 
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
		//did private methods to make neater and see where code fails
		jokerBpriv();
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {

		CardNode prev = deckRear.next;
		CardNode ptr = deckRear.next.next;
		//two special cases
		//not using private methods b/c thats where error is
		if(deckRear.next.cardValue == 27 || deckRear.next.cardValue == 28)
		{
			for(ptr = deckRear.next.next; ptr != deckRear; ptr = ptr.next)
			{
				if (deckRear.cardValue == 27 || deckRear.cardValue == 28)
				{
					return;
				}
				else if (ptr.cardValue == 27 || ptr.cardValue == 28)
				{
					CardNode temp = ptr;
					CardNode temp2 = ptr.next;
					deckRear = temp;
					deckRear.next = temp2;
					return;
				}
				else 
				{
					prev = prev.next;
				}
			}
		}
		else if (deckRear.cardValue == 27 || deckRear.cardValue == 28)
		{
			prev = deckRear;
			ptr = deckRear.next;

			for(ptr = deckRear.next;ptr != deckRear;ptr = ptr.next)
			{
				if(deckRear.next.cardValue == 27 || deckRear.next.cardValue == 28)
				{
					return;
				}
				else if(ptr.cardValue == 27 || ptr.cardValue == 28)
				{
					CardNode front = deckRear.next;
					CardNode temp = ptr;
					CardNode temp2 = prev;
					deckRear.next = front;
					deckRear = temp2;
					deckRear.next = temp;
					return;
				}
				else
				{
					prev = prev.next;
				}
			}
		}
		else
		{
			//perform the triple cut 
			//privaet method for cut
			nonSpecTripleCut();
		}
			
			/*//if one of the two special cases occurs then the tripleCutprivback or front method is called to deal with them
			//else statement is computed if none of the two special cases occur , else statement calls  nonSpecTripleCut();
			if(deckRear.cardValue == 27 || deckRear.cardValue == 28){
				tripleCutprivBack();
			}else if(deckRear.next.cardValue == 27 || deckRear.next.cardValue == 28){
				tripleCutprivFront();
			}else{
				nonSpecTripleCut();
			}
			
		*/
		}
		
	
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {	
		
			//gets value of last card and sets pointers to second to last num of list
			int value = deckRear.cardValue;
			if(deckRear.cardValue == 28){
				value = 27;
			}
			CardNode numBeforeLast = deckRear.next;
			while(numBeforeLast.next != deckRear){
				numBeforeLast = numBeforeLast.next;
			}

			//sets two more pointers to portion you are trying to move
			CardNode beginning= deckRear.next;
			CardNode end = deckRear.next;
			for(int x = 1; x!= value; x++ ){
				end = end.next;
			}

			//System.out.println("value of value: " + value );
			//System.out.println("value of numb4lst: " + numBeforeLast.cardValue );
			//System.out.println("value of beginn: " + beginning.cardValue );
			//System.out.println("value of end: " + end.cardValue );

			//takes the numbers corresponding to 'value' and moves them between numb4lst and deckRear
			deckRear.next = end.next;
			numBeforeLast.next = beginning;
			end.next = deckRear;
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		
		jokerA();//works fin
		jokerB();//works fine

		tripleCut();//works fine
		countCut();//works fine

		int keyVal = 0;
		int frontValue = deckRear.next.cardValue;
		if(frontValue == 28)
		{
			frontValue = 27;
		}
		//set pointer to find the key
		CardNode ptr = deckRear.next;
		
		for(int x = 0; x!= frontValue; x++ ){
			ptr = ptr.next;
		}

		if(ptr.cardValue == 27 ){
			keyVal = 27;
			while(keyVal == 27){
				jokerA();
				jokerB();
				tripleCut();
				countCut();
				ptr=deckRear.next;

				frontValue = deckRear.next.cardValue;

				if(frontValue == 28)
				{
					frontValue = 27;
				}
				for(int x = 0; x!= frontValue; x++ ){
					ptr = ptr.next;
				}
				keyVal = ptr.cardValue;
			}
			
		}else if(ptr.cardValue == 28){
			keyVal = 28;
			while(keyVal == 28){
				jokerA();
				jokerB();
				tripleCut();
				countCut();
				ptr=deckRear.next;

				frontValue = deckRear.next.cardValue;

				if(frontValue == 28)
				{
					frontValue = 27;
				}
				for(int x = 0; x!= frontValue; x++ ){
					ptr = ptr.next;
				}
				keyVal = ptr.cardValue;
			}
		}
		else
		{
			keyVal = ptr.cardValue;
		}
		return keyVal;
		
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {
	
	
		
		String result = "";
		
		int length = message.length();
		int enVal = 0;
		for(int x = 0; x <length; x++){
			char c = message.charAt(x);
			int ch = c-'A'+1;
			if(ch>0 && ch<27){
				enVal = getKey()+ch;
				
				if(enVal > 26){
					enVal = enVal- 26;
				}
				result = result + (char)(enVal-1+'A');
			}
			
		}
		return result;
		
	
	    
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		
		
		String result = "";
		int decVal = 0;
		int length = message.length();
		
		for(int x = 0; x <length; x++){
			char c = message.charAt(x);
			int ch = c-'A'+1;
			if(ch>0 && ch<27){
				int keyVal = getKey();
				decVal = ch-keyVal;
				//System.out.println("key val: "+ keyVal);
				if(decVal < 1){
					decVal = decVal+ 26;
				}
				//System.out.println(decVal);
				result += (char)(decVal-1+'A');
			}

		}
		
	    return result;
	}
	
	
	private void jokerBpriv(){
		//same as joker A but do the switch twice
				if(deckRear == null){
					return;
				}else{
					CardNode ptr = deckRear;
					if(ptr.cardValue == 28){
						ptr.cardValue = ptr.next.cardValue;
						ptr.next.cardValue = 28;
						ptr = ptr.next;
						ptr.cardValue = ptr.next.cardValue;
						ptr.next.cardValue = 28;
					}else{
						do{
							if(ptr.cardValue== 28){
								ptr.cardValue = ptr.next.cardValue;
								ptr.next.cardValue = 28;
								ptr = ptr.next;
								ptr.cardValue = ptr.next.cardValue;
								ptr.next.cardValue = 28;
								break;
							}else {
								ptr = ptr.next;
							}
						}while(ptr!=deckRear);
					}
				}
		
	}
	private void jokerApriv(){
		if(deckRear == null){
			return;
		}else{
			CardNode ptr = deckRear;
			if(ptr.cardValue == 27){
				ptr.cardValue = ptr.next.cardValue;
				ptr.next.cardValue = 27;
			}else{
				do{
					if(ptr.cardValue== 27){
						ptr.cardValue = ptr.next.cardValue;
						ptr.next.cardValue = 27;
						break;
					}else {
						ptr = ptr.next;
					}
				}while(ptr!=deckRear);
			}
		}
	}

	
	public void nonSpecTripleCut(){
		CardNode firstJoker = deckRear.next;
		CardNode secJoker = deckRear.next;
		CardNode numAfterSecJoker;
		int jokerval;
		//sets pointers to both joker locations
		while(firstJoker.cardValue != 27 && firstJoker.cardValue != 28 ){
			firstJoker = firstJoker.next;
		}
		if(firstJoker.cardValue == 27){
			jokerval = 28;
		}else{
			jokerval = 27;
		}

		while(secJoker.cardValue != jokerval ){
			secJoker = secJoker.next;
		}
		
		//initialize before and after joker
		numAfterSecJoker = secJoker.next;
		CardNode endoflist= numAfterSecJoker;
		while (endoflist != deckRear){
			endoflist = endoflist.next;
		}

		//printList(deckRear);
		//secJoker.next = beforeJoker;
		//printList(deckRear);
		//afterJoker.next = firstJoker;
		//printList(deckRear);
		//System.out.println();

		CardNode tmp = deckRear;
		while (tmp.next != firstJoker){
			tmp = tmp.next;
		}

		CardNode tmp2 = deckRear.next;
		deckRear = tmp;
		//good up to here

		deckRear.next = numAfterSecJoker;
		secJoker.next = tmp2;
		endoflist.next = firstJoker;
		//so far so good
		
	}
}

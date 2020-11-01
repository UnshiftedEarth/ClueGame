package clueGame;

public class Card {

	private String cardName;
	private CardType type;
	
	public Card() {
		
	}
	
	public Card(String cardName, CardType type) {
		super();
		this.cardName = cardName;
		this.type = type;
	}


	// setters and getters 
	public Object getName() {
		return cardName;
	}

	public CardType getType() {
		return type;
	}
}

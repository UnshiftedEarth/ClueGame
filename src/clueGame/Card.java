package clueGame;

public class Card {

	private String cardName;
	private CardType type;
	
	public Card() {
		this.cardName = "";
		type = CardType.WEAPON;
	}
	
	public Card(String cardName, CardType type) {
		super();
		this.cardName = cardName;
		this.type = type;
	}
	
	public boolean equals(Card target) {
		return target.getName().equals(cardName) && target.getType() == type;
	}


	// setters and getters 
	public Object getName() {
		return cardName;
	}

	public CardType getType() {
		return type;
	}
}

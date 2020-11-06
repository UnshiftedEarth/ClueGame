package clueGame;

public class Solution {

	public Card person;
	public Card room;
	public Card weapon;
	
	
	public Solution() {
		person = new Card();
		room = new Card();
		weapon = new Card();
	}
	
	public Solution(Card person, Card room, Card weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

}

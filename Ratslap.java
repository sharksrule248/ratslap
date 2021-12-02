import java.util.*;

public class Ratslap
{
	Scanner in;
	// card lists
	public ArrayList<Character> player;
	public ArrayList<Character> cpu1;
	public ArrayList<Character> cpu2;
	public ArrayList<Character> cpu3;
	public ArrayList<Character> deck;
	public ArrayList<Character> played;
	// global variables
	private double randMod1;
	private double randMod2;
	private int delay;
	public int cpu;
	public int whoTurn;
	public boolean freezeNext;
	public int willCollect;
	public int faceTurns;
	public boolean wasSlap;
	public boolean wasFace;
	public int whoSlap;
	public boolean lastChance;
	public boolean isJack;
	public static volatile boolean isIn;
	
	public Ratslap(Scanner in)
	{
		this.in = in;
		player = new ArrayList<>();
		cpu1 = new ArrayList<>();
		cpu2 = new ArrayList<>();
		cpu3 = new ArrayList<>();
		played = new ArrayList<>();
		deck = new ArrayList<>();
		randMod1 = 0;
		randMod2 = 0;
		delay = 0;
		cpu = 0;
		whoTurn = 0;
		whoSlap = 0;
		willCollect = 0;
		freezeNext = false;
		wasSlap = false;
		wasFace = false;
		lastChance = false;
		faceTurns = 5;
		isIn = false;
	}
	
	public void intro() //intro game/player choices, give option to call gameinfo before going into setup
	{
		System.out.println("Welcome to Egyptian Ratscrew!\nPlease press enter if you already know the rules, and ? if you need to learn them.");
		
		String choice = in.nextLine();
		if (!(choice.equals(""))) gameInfo();
		setup();
	}
	
	private void gameInfo() //show at first run (can be skipped if enter is pressed quick) as well as at request
	{
		System.out.println("Topic 1: General Play and Objective:\nThe general play of this game is to take turns playing cards from your hand (which you are not allowed to look at).\nThe objective is to get all the cards from the other players by taking the cards from the center as they build up.\nThere are two ways to do this: Face cards and Slapping.");
		System.out.println("Press enter to continue...");
		if (in.nextLine().equals(""))
		{
			System.out.println("Topic 2: Face cards:\nCard value in this game is simple, where only four cards are powerful.\nFace cards force the next player to play a certain number of cards in a row, and, if none of them are face cards, then whoever played the last face card takes the middle deck.\nJacks are the most powerful (1 try) while Aces are least (4 tries) so the power of cards goes:\n[J, Q, K, A], 10, 9, 8, 7, 6, 5, 4, 3, 2");
			System.out.println("Press enter to continue...");
			if (in.nextLine().equals(""))
			{
				System.out.println("Topic 3: Slapping:\nCards in the middle can also be taken by recognizing a slap and slapping the cards in the middle (In this game, slapping is pressing the enter key).\nThe slaps that are recognized in this game are pairs (any two of the same card value next to each other (2 & 2)) and sandwiches (any two of the same card value separated by one random card (6 & 4 & 6)).");
				System.out.println("Press enter to continue...");
				if (in.nextLine().equals(""))
				{
					System.out.println("Topic 4: Wrap-up:\nAdditionally, your deck will not be shuffled at any point, and new cards will be added in from the bottom.");
					System.out.println("Good luck! Proceeding to game setup...");
				}
			}
		}
	}
	
	public void setup() //# of bots, difficulty level, etc.
	{
		System.out.println("Please choose an additional 2 or 3 cpu opponents.");
		
		while (cpu < 2 || cpu > 3)
		{
			cpu = in.nextInt();
			if (cpu != 2 && cpu != 3) System.out.println("Incorrect option, enter again.");
		}
		
		System.out.println("\nPlease choose a difficulty level.\n1. Very easy\n2. Easy\n3. Normal\n4. Hard\n5. Lunatic");
		int difficulty = 0;
		while (difficulty <= 0 || difficulty >= 6)
		{
			difficulty = in.nextInt();
			if (difficulty == 5)
			{
				System.out.println("I hope you're ready!");
			}
			if (difficulty <= 0 || difficulty >= 6) System.out.println("Incorrect option, enter again.");
		}
		setDifficulty(difficulty);
	}
	
	private void setDifficulty(int _difficulty)
	{
		int difficulty = _difficulty;
		switch (difficulty) {
			case 1:
				randMod1 = 2;
				randMod2 = 3.5;
				delay = 2000;
				break;
			case 2:
				randMod1 = 1.2;
				randMod2 = 2.2;
				delay = 1600;
				break;
			case 3:
				randMod1 = .7;
				randMod2 = 1.3;
				delay = 1200;
				break;
			case 4:
				randMod1 = .4;
				randMod2 = .9;
				delay = 750;
				break;
			default:
				randMod1 = .1;
				randMod2 = .33;
				delay = 333;
				break;
		}
	}
	
	private double grabDelay() //call multiple times
	{
		double randDelay;
		randDelay = randMod1 + ((randMod2-randMod1)*Math.random());
		return randDelay;
	}
	
	public void initCards() //call once
	{
		char[] suit = {'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K'};
		for (int j = 0; j < 4; j++)
		{
			for (int i = 0; i < 13; i++)
			{
				deck.add(suit[i]);
			}
		}
		
		shuffle();
		shuffle();
		shuffle();
		
		
		if (cpu == 2)
		{
			int cnt2 = 0;
			int deck2 = 51;
			while (deck.size() > 0)
			{
				if (cnt2 % 3 == 0) player.add(deck.get(deck2));
				else if (cnt2 % 3 == 1) cpu1.add(deck.get(deck2));
				else if (cnt2 % 3 == 2) cpu2.add(deck.get(deck2));
				deck.remove(deck2);
				cnt2++;
				deck2--;
			}
		}
		else if (cpu == 3)
		{
			int cnt3 = 0;
			int deck3 = 51;
			while (deck.size() > 0)
			{
				if (cnt3 % 4 == 0) player.add(deck.get(deck3));
				else if (cnt3 % 4 == 1) cpu1.add(deck.get(deck3));
				else if (cnt3 % 4 == 2) cpu2.add(deck.get(deck3));
				else if (cnt3 % 4 == 3) cpu3.add(deck.get(deck3));
				deck.remove(deck3);
				cnt3++;
				deck3--;
			}
		}
		
		System.out.println("Decks dealed. Begin!\n");
	}
	
	public void winInfo(int _turns, int _winner)
	{
		switch (_winner)
		{
			case 0:
				System.out.println("Congratulations Player! You have got all the cards!\nTotal turns: " + _turns);
				break;
			case 1:
				System.out.println("Sorry Player, CPU 1 got all the cards...\nTotal turns: " + _turns);
				break;
			case 2:
				System.out.println("Sorry Player, CPU 2 got all the cards...\nTotal turns: " + _turns);
				break;
			case 3:
				System.out.println("Sorry Player, CPU 3 got all the cards...\nTotal turns: " + _turns);
				break;
			default:
				System.out.println("This isn't supposed to happen...");
				break;
		}
	}
	
	public List<Character> getList0()
	{
		return player;
	}
	
	public List<Character> getList1()
	{
		return cpu1;
	}
	
	public List<Character> getList2()
	{
		return cpu2;
	}
	
	public List<Character> getList3()
	{
		return cpu3;
	}
	
	public List<Character> getListDeck()
	{
		return deck;
	}
	
	public void shuffle() //call multiple times
	{
		int[] index = genIndexSwap(deck);
		ArrayList<Character> _newcards = new ArrayList<>();
		
		for (int i = 0; i < deck.size(); i++)
		{
			_newcards.add(deck.get(index[i]));
		}
		deck = new ArrayList<Character>(_newcards);
	}
	
	private int[] genIndexSwap(ArrayList<Character> _sizer)
	{
		int[] indexSwap = new int[_sizer.size()];
		int usedValidSize = 0;
		boolean isRepeat = true;
		while (usedValidSize < _sizer.size())
		{
			while (isRepeat)
			{
			int rand = (int) (Math.random()*indexSwap.length);
			isRepeat = false;
			
			if (usedValidSize > 0)
			{
				for (int i = 0; i < usedValidSize; i++)
				{
					if (rand == indexSwap[i]) isRepeat = true;
				}
			}
			
			if (!isRepeat)
			{
				indexSwap[usedValidSize] = rand;
				usedValidSize++;
			}
			}
			isRepeat = true;
		}
		return indexSwap;
	}
	
	private boolean checkIndex(int[] _arr)
	{
		boolean noRepeat = false;
		int cnt = 0;
		for (int i = 0; i < _arr.length; i++)
		{
			for (int j = 0; j < _arr.length; j++)
			{
				if (i != j)
				{
					if (_arr[i] == _arr[j])
					{
						cnt++;
					}
				}
			}
		}
		if (cnt == 0) noRepeat = true;
		else noRepeat = false;
		return noRepeat;
	}
	
	private boolean isFace(char _card)
	{
		boolean isFace;
		if (_card == 'A' || _card == 'J' || _card == 'Q' || _card == 'K') isFace = true;
		else isFace = false;
		return isFace;
	}
	
	private boolean isSandwich()
	{
		boolean isSand = false;
		if (played.size() > 2)
		{
			if (played.get(played.size() - 1) == played.get(played.size() - 3)) isSand = true;
			else isSand = false;
		}
		return isSand;
	}
	
	private boolean isPair()
	{
		boolean isPair = false;
		if (played.size() > 1)
		{
			if (played.get(played.size() - 1) == played.get(played.size() - 2)) isPair = true;
			else isPair = false;
		}
		return isPair;
	}
	
	private void faceTurns()
	{
		char face = played.get(played.size() - 1);
		switch (face)
		{
			case 'J':
				faceTurns = 1;
				break;
			case 'Q':
				faceTurns = 2;
				break;
			case 'K':
				faceTurns = 3;
				break;
			case 'A':
				faceTurns = 4;
				break;
		}
	}
	
	public void collectPlayed(int _collector) //send cards in played to the back of _collector's hand.
	{
		switch (_collector)
		{
			case 0:
				while (played.size() > 0)
				{
					player.add(0, played.get(played.size() - 1));
					played.remove(played.size() - 1);
				}					
				break;
			case 1:
				while (played.size() > 0)
				{
					cpu1.add(0, played.get(played.size() - 1));
					played.remove(played.size() - 1);
				}	
				break;
			case 2:
				while (played.size() > 0)
				{
					cpu2.add(0, played.get(played.size() - 1));
					played.remove(played.size() - 1);
				}	
				break;
			case 3:
				while (played.size() > 0)
				{
					cpu3.add(0, played.get(played.size() - 1));
					played.remove(played.size() - 1);
				}	
				break;
		}
		faceTurns = 5;
	}
	
	public void slap() //grab delay, set timer, try to collect player input, if player slapped first then call collectPlayed(0) otherwise know which CPU was fastest.
	{
		isIn = false;
		double max = 0;
		if (cpu == 2)
		{
			double d1 = 1000 * grabDelay();
			double d2 = 1000 * grabDelay();
			if (d1 >= d2)
			{
				whoSlap = 1;
				max = d1;
			}
			else if (d2 >= d1)
			{
				whoSlap = 2;
				max = d2;
			}
		}
		else if (cpu == 3)
		{
			double d1 = 1000 * grabDelay();
			double d2 = 1000 * grabDelay();
			double d3 = 1000 * grabDelay();
			if (d1 >= d2 && d1 >= d3)
			{
				whoSlap = 1;
				max = d1;
			}
			else if (d2 >= d1 && d2 >= d3)
			{
				whoSlap = 2;
				max = d2;
			}
			else if (d3 >= d1 && d3 >= d2)
			{
				whoSlap = 3;
				max = d3;
			}
		}
		
		Thread inputThread = new Thread(new Runnable() {
			public void run() {
				Scanner scan = new Scanner(System.in);
				String input = "10";
				while (!isIn)
				{
					input = scan.nextLine();
					if (input != "10") isIn = true;
				}
			}
		});
		
		long start = System.currentTimeMillis();
		long delay = (long) max;
		
		inputThread.start();
		
		while (start + delay >= System.currentTimeMillis() && !isIn)
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long finish = System.currentTimeMillis();
		
		if (finish <= start + delay)
		{
			whoSlap = 0;
			System.out.println("You got the slap! :)");
		}
		else
		{
			System.out.println("You no win slap... :(");
		}
		
		collectPlayed(whoSlap);
	}
	
	public void turn()
	{
		wasFace = false;
		wasSlap = false;
		
		if ((player.size() == 0 || cpu1.size() == 0 || cpu2.size() == 0 || cpu3.size() == 0) && freezeNext) //catch the possiblity of someone being unable to complete their cards from face, in which case the responibility is handed off to the next player for the rest of the cards.
		{
			if (cpu == 2)
			{
				if (player.size() == 0 && whoTurn == 0) whoTurn = 1;
				if (cpu1.size() == 0 && whoTurn == 1) whoTurn = 2;
				if (cpu2.size() == 0 && whoTurn == 2) whoTurn = 0;
			}
			else if (cpu == 3)
			{
				if (player.size() == 0 && whoTurn == 0) whoTurn = 1;
				if (cpu1.size() == 0 && whoTurn == 1) whoTurn = 2;
				if (cpu2.size() == 0 && whoTurn == 2) whoTurn = 3;
				if (cpu3.size() == 0 && whoTurn == 3) whoTurn = 0;
			}
		}
		
		switch (whoTurn) //actual turn regarding player whoTurn
		{
			case 0:
				played.add(player.get(player.size() - 1));
				player.remove(player.size() - 1);
				displayCards();
				if (isPair() || isSandwich())
				{
					slap();
					whoTurn = whoSlap;
					wasSlap = true;
				}
				else if (isFace(played.get(played.size() - 1)))
				{
					faceTurns();
					wasFace = true;
					willCollect = whoTurn;
				}
				break;
			case 1:
				played.add(cpu1.get(cpu1.size() - 1));
				cpu1.remove(cpu1.size() - 1);
				displayCards();
				if (isPair() || isSandwich())
				{
					slap();
					whoTurn = whoSlap;
					wasSlap = true;
				}
				else if (isFace(played.get(played.size() - 1)))
				{
					faceTurns();
					wasFace = true;
					willCollect = whoTurn;
				}
				break;
			case 2:
				played.add(cpu2.get(cpu2.size() - 1));
				cpu2.remove(cpu2.size() - 1);
				displayCards();
				if (isPair() || isSandwich())
				{
					slap();
					whoTurn = whoSlap;
					wasSlap = true;
				}
				else if (isFace(played.get(played.size() - 1)))
				{
					faceTurns();
					wasFace = true;
					willCollect = whoTurn;
				}
				break;
			case 3:
				played.add(cpu3.get(cpu3.size() - 1));
				cpu3.remove(cpu3.size() - 1);
				displayCards();
				if (isPair() || isSandwich())
				{
					slap();
					whoTurn = whoSlap;
					wasSlap = true;
				}
				else if (isFace(played.get(played.size() - 1)))
				{
					faceTurns();
					wasFace = true;
					willCollect = whoTurn;
				}
				break;
		} //done with turn action, time for logic
		
		if (!wasSlap && !wasFace && faceTurns > 4 && freezeNext)
		{
			freezeNext = false;
		}
		
		boolean incPlayer = true;
		
		if (!wasFace && !wasSlap && freezeNext)
		{
			faceTurns--;
			incPlayer = false;
		}
		if (!wasFace && freezeNext && faceTurns == 0)
		{
			System.out.println("face card expired!: giving to " + whoIs(willCollect) + ".");
			collectPlayed(willCollect);
			whoTurn = willCollect;
			incPlayer = false;
		}
		if (wasFace && !wasSlap)
		{
			incPlayer = true;
		}
		if (wasSlap)
		{
			incPlayer = false;
		}
		
		if (incPlayer)
		{
			whoTurn++;
			if (cpu == 2)
			{
				if (whoTurn == 3) whoTurn = 0;
				if (player.size() == 0 && whoTurn == 0) whoTurn = 1;
				if (cpu1.size() == 0 && whoTurn == 1) whoTurn = 2;
				if (cpu2.size() == 0 && whoTurn == 2) whoTurn = 0;
			}
			else if (cpu == 3)
			{
				if (whoTurn == 4) whoTurn = 0;
				if (player.size() == 0 && whoTurn == 0) whoTurn = 1;
				if (cpu1.size() == 0 && whoTurn == 1) whoTurn = 2;
				if (cpu2.size() == 0 && whoTurn == 2) whoTurn = 3;
				if (cpu3.size() == 0 && whoTurn == 3) whoTurn = 0;
			}
		}
		
		if (!wasFace && !wasSlap && freezeNext && faceTurns == 0)
		{
			freezeNext = false;
			
		}
		if (wasFace && !wasSlap)
		{
			freezeNext = true;
		}
		if (wasSlap)
		{
			freezeNext = false;
		}
		
		try {
			Thread.sleep(delay);
		} catch (Exception e) {
		}
	}
	
	public String whoIs(int _id)
	{
		String who = "";
		if (_id == 0) who = "Player";
		else if (_id == 1) who = "CPU1";
		else if (_id == 2) who = "CPU2";
		else if (_id == 3) who = "CPU3";
		return who;
	}
	
	public void displayCards()
	{
		String playing = "";
		String whoPlaying = "";
		switch (whoTurn)
		{
			case 0:
				whoPlaying = "<--Player";
				break;
			case 1:
				whoPlaying = "<--CPU1";
				break;
			case 2:
				whoPlaying = "<--CPU2";
				break;
			case 3:
				whoPlaying = "<--CPU3";
				break;
		}
		for (int i = 0; i < played.size(); i++)
		{
			if (i == played.size() - 1)
			{
				if (played.get(i) == '0')
				{
					playing = playing + "10" + whoPlaying + "\n";
				}
				else playing = playing + played.get(i) + whoPlaying + "\n";
			}
			else
			{
				if (played.get(i) == '0')
				{
					playing = playing + "10" + " ";
				}
				else playing = playing + played.get(i) + " ";
			}
		}
		if (freezeNext)
		{
			playing = playing + whoIs(whoTurn) + "'s chances left: " + (faceTurns - 1) + "\n";
		}
		else
		{
			playing = playing + "\n";
		}
		System.out.println(playing);
	}
}
import java.util.*;

public class game
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		Ratslap game1 = new Ratslap(in);
		
		game1.intro();
		System.out.println("Initializing Cards...");
		game1.initCards();
		
		int counter = 0;
		boolean stop = false;
		int whoWin = 0;
		
		while (!stop)
		{
			game1.turn();
			int cont = 0;
			if (game1.cpu == 2)
			{
				if (game1.getList0().size() == 0) cont++;
				if (game1.getList1().size() == 0) cont++;
				if (game1.getList2().size() == 0) cont++;
				if (cont == 2) stop = true;
			}
			else if (game1.cpu == 3)
			{
				if (game1.getList0().size() == 0) cont++;
				if (game1.getList1().size() == 0) cont++;
				if (game1.getList2().size() == 0) cont++;
				if (game1.getList3().size() == 0) cont++;
				if (cont == 3) stop = true;
			}
			
			if (!stop)
			{
				System.out.println("player cards: " + game1.getList0().size());
				System.out.println("CPU1 cards: " + game1.getList1().size());
				System.out.println("CPU2 cards: " + game1.getList2().size());
				if (game1.cpu == 3) System.out.println("CPU3 cards: " + game1.getList3().size());
				System.out.println("\nAdvancing to turn " + (counter + 1) + "...\n");
				counter++;
			}
			else
			{
				int max = 0;
				if (max < game1.getList0().size()) max = game1.getList0().size();
				if (max < game1.getList1().size()) max = game1.getList1().size();
				if (max < game1.getList2().size()) max = game1.getList2().size();
				if (max < game1.getList3().size()) max = game1.getList3().size();
				
				if (max == game1.getList0().size()) whoWin = 0;
				else if (max == game1.getList1().size()) whoWin = 1;
				else if (max == game1.getList2().size()) whoWin = 2;
				else if (max == game1.getList3().size()) whoWin = 3;
			}
		}
		
		if (whoWin == 0)
		{
			game1.winInfo(counter, 0);
		}
		if (whoWin == 1)
		{
			game1.winInfo(counter, 1);
		}
		if (whoWin == 2)
		{
			game1.winInfo(counter, 2);
		}
		if (whoWin == 3)
		{
			game1.winInfo(counter, 3);
		}
		
		System.out.println("Good game! Press enter to exit.");
		
		String exitir = in.nextLine();
		if (in.nextLine().equals(""));
		{
			System.out.println("Exiting!");
		}
	}
}
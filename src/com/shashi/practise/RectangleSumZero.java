package com.shashi.practise;

public class RectangleSumZero {

	public static void main(String[] args) {

		int array[][] = new int[][] {
		{ 2, 3, 5, 2 },
		{ -1, 2, 3, -4 },
		{ 2, -3, -5, 2 },
		{ -1, 2, 3, -4 }
		};

		findZero(array, 4, 4);
	}

	public static void findZero(int[][] array, int row, int col)

	{
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < col; j++)
			{
				for (int ii = row - 1; ii > i; ii--)
				{
					for (int jj = col - 1; jj > j; jj--)
					{
						findSum(array, i, j, ii, jj);
					}
				}
			}
		}
	}

	private static void findSum(int[][] array, int startingX, int startingY,
			int endingX, int endingY) {

		int sum = 0;
		int x = startingX;
		int y = startingY;
		try {
			while (startingY <= endingY)
			{
				sum = sum + array[startingX][startingY];
				sum = sum + array[endingX][startingY++];
			}
			startingY = y;
			while (startingX <= endingX)
			{
				sum = sum + array[startingX][startingY];
				sum = sum + array[startingX++][endingY];
			}
		}
		catch (Exception ex)
		{
			System.out.println("Exception [" + startingX + "] , [" + startingY
					+ "] \n[" + endingX + "] , [" + endingY + "]");
		}
		startingX = x;
		sum = sum - array[startingX][startingY] - array[startingX][endingY]
				- array[endingX][endingY] - array[endingX][startingY];
		//System.out.println(sum);
		if (sum == 0)
		{
			System.out.println("[" + startingX + "] , [" + startingY + "] \n["
					+ endingX + "] , [" + endingY + "]");
			System.out.println();
		}
	}
}

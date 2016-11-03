using System;

/// <summary>
/// Programming assignment for Week 1.
/// This application will calculate the distance between two points and the angle between those points.
/// </summary>
public class Program
{
	/// <summary>
	/// Main method of the application that will handle all logic.
	/// <summary>
	public static void Main()
	{
		// welcome message
		Console.WriteLine("Hello, this program will help you calculate the distance and the angle between two points.");
		Console.WriteLine();
		
		// request first point's coordenates
		Console.WriteLine("Please enter the X coordinate of the first point: ");
		float point1X = float.Parse(Console.ReadLine());
		Console.WriteLine("Please enter the Y coordinate of the first point: ");
		float point1Y = float.Parse(Console.ReadLine());
		Console.WriteLine("The first point will be: (" + point1X + "," + point1Y + ").");
		Console.WriteLine();
		
		// request second point's coordenates
		Console.WriteLine("Please enter the X coordinate of the second point: ");
		float point2X = float.Parse(Console.ReadLine());
		Console.WriteLine("Please enter the Y coordinate of the second point: ");
		float point2Y = float.Parse(Console.ReadLine());
		Console.WriteLine("The second point will be: (" + point2X + "," + point2Y + ").");
		Console.WriteLine();
		
		// compute deltas
		float deltaX = point2X - point1X;
		float deltaY = point2Y - point1Y;
		
		// compute distance
		double distance = Math.Sqrt(Math.Pow(deltaX, 2) + Math.Pow(deltaY, 2));
		
		// compute angle
		double angle = Math.Atan2(deltaY, deltaX) * (180.0 / Math.PI);
		
		// output results
		Console.WriteLine("The distance between points (" + point1X + "," + point1Y + ") and (" + point2X + "," + point2Y + ") is : " + distance.ToString("F3"));
		Console.WriteLine("The angle between points (" + point1X + "," + point1Y + ") and (" + point2X + "," + point2Y + ") is : " + angle.ToString("F3"));
	}
}

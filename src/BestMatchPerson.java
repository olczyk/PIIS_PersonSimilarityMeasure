import java.util.Comparator;


public class BestMatchPerson {
	
	double similarityRate;
	int pertsonId;
	
	BestMatchPerson(double similarityRate, int pertsonId)
	{
		this.similarityRate = similarityRate;
		this.pertsonId = pertsonId;
	}
	
	public static Comparator<BestMatchPerson> SimilarityRateComparator = new Comparator<BestMatchPerson>()
	{
		public int compare(BestMatchPerson bestMatch1, BestMatchPerson bestMatch2) 
		{
			double similarityRate1 = bestMatch1.similarityRate;
			double similarityRate2 = bestMatch2.similarityRate;
			
			return Double.compare(similarityRate1, similarityRate2);
		}
	};

}

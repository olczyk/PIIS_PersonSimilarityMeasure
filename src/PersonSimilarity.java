import java.util.ArrayList;
import java.util.Collections;


public class PersonSimilarity {
	
	ArrayList<Evaluation> Evaluations;
	ArrayList<Evaluation> MissingEvaluations;
	ArrayList<Movie> Movies;
	
	PersonSimilarity(ArrayList<Evaluation> Evaluations, ArrayList<Evaluation> MissingEvaluations, ArrayList<Movie> Movies)
	{
		this.Evaluations = Evaluations;
		this.MissingEvaluations = MissingEvaluations;
		this.Movies = Movies;
	}

	public ArrayList<Evaluation> PredictEvaluations()
	{
		for(int i=5; i<1817; i++) //iterating through each person (I know the range from the observation)
		{
			ArrayList<Evaluation> PersonEvaluations = GetPersonEvaluations(i, Evaluations);
			ArrayList<Evaluation> PersonMissingEvaluations = GetPersonEvaluations(i, MissingEvaluations);
			
			if(PersonMissingEvaluations.size() > 0 && PersonEvaluations.size() > 0)
			{
				System.out.println("*********** Person ID = " + i + " ***********");
				
				ArrayList<BestMatchPerson> BestPersonMatches = Get10MostSimilarPeople(i, PersonEvaluations);
				
				for(int j=0; j<PersonMissingEvaluations.size(); j++) //iterating through the not evaluated movies
				{
					int evaluationId = PersonMissingEvaluations.get(j).id;
					int movieId = PersonMissingEvaluations.get(j).movieId;

					PersonMissingEvaluations.get(j).evaluation = GetPrediction(movieId, BestPersonMatches);
					FillMissingEvaluation(evaluationId, PersonMissingEvaluations.get(j).evaluation);
				}
			}
			else
			{
				if(PersonMissingEvaluations.size() == 0)
				{
					System.out.println("WARNING! Person ID = " + i + " has evaluated all of the movies.");
				}
				
				if(PersonEvaluations.size() == 0)
				{
					System.out.println("WARNING! Person ID = " + i + " has not evaluated any movie. No data for predictions.");
				}
			}
		}
		return MissingEvaluations;
	}
	
	private ArrayList<BestMatchPerson> Get10MostSimilarPeople(int examinedPersonId, ArrayList<Evaluation> ExaminedPersonEvaluations)
	{
		ArrayList<BestMatchPerson> BestPersonMatches = new ArrayList<BestMatchPerson>();
		
		int counter = 1;
		
		for(int i=5; i<1817; i++)
		{
			if(i != examinedPersonId)
			{
				ArrayList<Evaluation> PersonEvaluations = GetPersonEvaluations(i, Evaluations);
				
				double similarityRate = 0;
				
				for(int j=0; j<ExaminedPersonEvaluations.size(); j++)
				{
					int currentEvaluatedMovieId = ExaminedPersonEvaluations.get(j).movieId;
					
					Evaluation personEvaluation = GetEvaluationByMovieId(currentEvaluatedMovieId, PersonEvaluations);
					
					if(personEvaluation != null)
					{
						similarityRate += CompareEvaluations(personEvaluation, ExaminedPersonEvaluations.get(j));
					}
					else
					{
						similarityRate += 0.2; //if the movie hasn't been evaluated
					}
				}
				
				if(counter<10)
				{
					BestMatchPerson bestMatch = new BestMatchPerson(similarityRate, i);
					BestPersonMatches.add(bestMatch);
				}
				else if(similarityRate > BestPersonMatches.get(0).similarityRate)
				{
					BestMatchPerson bestMatch = new BestMatchPerson(similarityRate, i);
					BestPersonMatches.remove(0);
					BestPersonMatches.add(bestMatch);
				}	
				Collections.sort(BestPersonMatches, BestMatchPerson.SimilarityRateComparator);
				
				counter++;
			}
		}
		return BestPersonMatches;
	}
	
	private Evaluation GetEvaluationByMovieId(int movieId, ArrayList<Evaluation> Evaluations)
	{
		Evaluation evaluation = null;
		
		for(int i=0; i<Evaluations.size();i++)
		{
			if(Evaluations.get(i).movieId == movieId)
			{
				evaluation = Evaluations.get(i);
				break;
			}
		}
		return evaluation;
	}
	
	private double CompareEvaluations(Evaluation evaluation1, Evaluation evaluation2)
	{
		double similarityRate = 0;
		
		int eval1 = Integer.parseInt(evaluation1.evaluation);
		int eval2 = Integer.parseInt(evaluation2.evaluation);
		
		if(Math.abs(eval1 - eval2) == 0)
		{
			similarityRate = 1;
		}
		else if(Math.abs(eval1 - eval2) <= 3)
		{
			similarityRate = 0.5;
		}		
		return similarityRate;
	}
	
	private String GetPrediction(int movieId, ArrayList<BestMatchPerson> BestPersonMatches)
	{
		String evaluation = "NULL";
		
		for(int i=9; i>=0;i--)
		{
			Evaluation bestPersonEvaluation = GetPersonEvaluationByMovieId(BestPersonMatches.get(i).pertsonId, movieId);
			
			if(bestPersonEvaluation != null)
			{
				evaluation = bestPersonEvaluation.evaluation;
				break;
			}
		}
		
		if(evaluation.equals("NULL"))
		{
			evaluation = "3"; //if any of the 1- most similar people hasn'y rated the movie, we are giving the 3 note
		}
		return evaluation;
	}
	
	private Evaluation GetPersonEvaluationByMovieId(int personId, int movieId)
	{
		Evaluation evaluation = null;
		
		for(int i=0; i<Evaluations.size();i++)
		{
			if(Evaluations.get(i).personId == personId && Evaluations.get(i).movieId == movieId)
			{
				evaluation = Evaluations.get(i);
			}
		}
		return evaluation;
	}
	
	private ArrayList<Evaluation> GetPersonEvaluations(int personId, ArrayList<Evaluation> Evaluations)
	{
		ArrayList<Evaluation> PersonEvaluations = new ArrayList<Evaluation>();
		
		for(int i=0;i<Evaluations.size();i++)
		{
			if(personId == Evaluations.get(i).personId)
			{
				PersonEvaluations.add(Evaluations.get(i));
			}
		}
		return PersonEvaluations;
	}
	
	private void FillMissingEvaluation(int evaluationId, String evaluation)
	{
		for(int i=0; i<MissingEvaluations.size();i++)
		{
			if(MissingEvaluations.get(i).id == evaluationId)
			{
				MissingEvaluations.get(i).evaluation = evaluation;
				return;
			}
		}
	}
}

import java.io.IOException;
import java.util.ArrayList;


public class Main {

	static final String moviesFileName = "records.csv";
	static final String evaluationsFileName = "train.csv";
	static final String missingEvaluationsFileName = "task.csv";
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		Utilities util = new Utilities();
		ArrayList<Movie> Movies = util.GetMoviesFromFile(moviesFileName);
		ArrayList<Evaluation> Evaluations = util.GetEvaluations(evaluationsFileName);
		ArrayList<Evaluation> MissingEvaluations = util.GetMissingEvaluations(missingEvaluationsFileName);
		
		PersonSimilarity ps = new PersonSimilarity(Evaluations, MissingEvaluations, Movies);
		ArrayList<Evaluation> PredictedEvaluations = ps.PredictEvaluations();
		
		util.SavePredictedEvaluationsToFile("result10.csv", PredictedEvaluations);
	}
}
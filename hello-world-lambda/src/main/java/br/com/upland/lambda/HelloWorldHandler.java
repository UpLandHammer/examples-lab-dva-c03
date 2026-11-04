package br.com.upland.lambda;

import br.com.upland.lambda.domain.DefaultResponse;
import br.com.upland.lambda.domain.WorkLoadParams;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class HelloWorldHandler implements RequestHandler<WorkLoadParams, DefaultResponse> {

    @Override
    public DefaultResponse handleRequest(WorkLoadParams input, Context context) {
        String message = String.format("Executing the program with %s seconds \n", input.workloadTimeSeconds());
        String workLoadMessage = heavyWorkLoad(input, context);
        String allMessage = message.concat(workLoadMessage);
        return new DefaultResponse(allMessage);
    }

    private String heavyWorkLoad(WorkLoadParams workLoadParams, Context context) {
        int durationSeconds = workLoadParams.workloadTimeSeconds();

        durationSeconds = Math.clamp(durationSeconds, 1, 840);

        long startTime = System.nanoTime();
        long durationNanos = durationSeconds * 1_000_000_000L;

        long number = 2;
        long primesFound = 0;
        long iterations = 0;

        context.getLogger().log("Starting CPU workload: "
                + durationSeconds + " seconds\n");

        while (System.nanoTime() - startTime < durationNanos) {

            if (isPrime(number)) {
                primesFound++;
            }

            number++;
            iterations++;
        }

        long elapsedMillis =
                (System.nanoTime() - startTime) / 1_000_000;

        String result = String.format(
                "Workload completed! Duration: %d ms, Iterations: %d, Primes found: %d",
                elapsedMillis,
                iterations,
                primesFound
        );

        context.getLogger().log(result + "\n");

        return result;

    }

    private boolean isPrime(long number) {

        if (number < 2) return false;

        for (long i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

}

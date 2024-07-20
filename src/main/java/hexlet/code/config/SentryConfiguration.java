package hexlet.code.config;

import io.sentry.Sentry;
import io.sentry.spring.jakarta.EnableSentry;
import org.springframework.context.annotation.Configuration;

@EnableSentry(dsn = "https://db8701394b8d7134cebecc8bff22b229@o4507635835666432.ingest.de.sentry.io/4507636270497872")
@Configuration
class SentryConfiguration {
    public void test() {
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }

}

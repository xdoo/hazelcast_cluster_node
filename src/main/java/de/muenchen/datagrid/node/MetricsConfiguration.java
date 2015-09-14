package de.muenchen.datagrid.node;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author claus.straube
 */
@Configuration
public class MetricsConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleStatsService.class);

    @Bean
    public MetricRegistry metricRegistry() {
        MetricRegistry metrics = new MetricRegistry();
        this.startReport(metrics);
        return metrics;
    }

    private void startReport(MetricRegistry metrics) {
        LOG.info("starting console reporter...");
        ConsoleReporter console = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        console.start(30, TimeUnit.SECONDS);
        
        JmxReporter jmx = JmxReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        jmx.start();
        
    }

}

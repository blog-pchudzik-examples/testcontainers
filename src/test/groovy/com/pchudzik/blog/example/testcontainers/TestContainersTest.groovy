package com.pchudzik.blog.example.testcontainers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification
import spock.lang.Subject

@Testcontainers
class TestContainersTest extends Specification {
    private static final Logger log = LoggerFactory.getLogger(TestContainersTest)

    private GenericContainer redis = new GenericContainer<>("redis:5.0.3-alpine")
            .withLogConsumer(new Slf4jLogConsumer(log))
            .withExposedPorts(6379)
            .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*", 1))

    @Subject
    private KeyValueStorage keyValueStorage

    void setup() {
        keyValueStorage = new KeyValueStorage(
                redis.containerIpAddress,
                redis.firstMappedPort)
    }

    def "puts value to storage"() {
        when:
        keyValueStorage.set("asd", "zxc")

        then:
        keyValueStorage.get("asd") == Optional.of("zxc")
    }

    def "gets value from storage"() {
        when:
        keyValueStorage.set("asd", "zxc")

        then:
        keyValueStorage.get("asd") == Optional.of("zxc")
        keyValueStorage.get("missing key") == Optional.empty()
    }
}

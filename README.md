# Coinbase Java Core

[![Maven Central](https://img.shields.io/maven-central/v/com.coinbase.core/coinbase-core-java)](https://central.sonatype.com/artifact/com.coinbase.core/coinbase-core-java)
[![Javadoc](https://javadoc.io/badge2/com.coinbase.core/coinbase-core-java/latest.svg)](https://javadoc.io/doc/com.coinbase.core/coinbase-core-java)

## Overview

*Coinbase Java Core* provides a centralized and reusable implementation for making HTTP requests and handling API responses for Coinbase Java SDKs. It includes support for custom headers, credentials, structured error handling, and JSON mapping via Jackson (including JSR-310 date/time types).

**Repository:** [coinbase-samples/core-java](https://github.com/coinbase-samples/core-java)

## Requirements

- **JDK** 11 or later

## Installation

Binaries and dependency information for Maven, Gradle, Ivy, and others are available on [Maven Central](https://central.sonatype.com/search?q=g%3Acom.coinbase.core+a%3Acoinbase-core-java&smo=true).

Maven:

```xml
<dependency>
    <groupId>com.coinbase.core</groupId>
    <artifactId>coinbase-core-java</artifactId>
    <version>x.y.z</version>
</dependency>
```

Gradle:

```gradle
implementation 'com.coinbase.core:coinbase-core-java:x.y.z'
```

Replace `x.y.z` with the [latest version on Maven Central](https://central.sonatype.com/artifact/com.coinbase.core/coinbase-core-java).

## Usage

Add the dependency, then import and use types from `com.coinbase.core`. Product-specific SDKs typically extend `CoinbaseNetHttpClient` and `CoinbaseServiceImpl` and supply a `CoinbaseCredentials` implementation.

```java
import com.coinbase.core.client.CoinbaseClient;
```

Use `CoinbaseClient` (and related abstractions) through your SDK’s client implementation.

### Architecture (summary)

| Abstraction | Role |
|-------------|------|
| `CoinbaseClient` | HTTP client contract; implemented by `CoinbaseNetHttpClient` (Java 11 `HttpClient`) |
| `CoinbaseService` | Service-layer contract; base implementation `CoinbaseServiceImpl` |
| `CoinbaseCredentials` | Auth headers for requests |
| `CoinbaseClientException` / `CoinbaseException` | Client and API error handling |

Packages live under `com.coinbase.core` (`client`, `common`, `credentials`, `errors`, `service`, `utils`).

### Extending this library (SDK authors)

1. Implement `CoinbaseCredentials` for your auth method.
2. Extend `CoinbaseNetHttpClient` and implement `handleResponse()` for product-specific error parsing.
3. Extend `CoinbaseServiceImpl` for each API service or resource.
4. Pass your client instance into service constructors.

## Development

Build and install locally:

```bash
mvn clean install
```

Generate Javadocs locally:

```bash
mvn javadoc:javadoc
```

Published API docs: [javadoc.io — coinbase-core-java](https://javadoc.io/doc/com.coinbase.core/coinbase-core-java).

Publishing to Maven Central (maintainers, requires GPG and credentials): see [DEPLOY.md](DEPLOY.md) or run `mvn clean deploy`.

## 🚨 Security and Bug Reports

If you discover a security vulnerability within this library, please see our [Security Policy](SECURITY.md) for disclosure information.

## 📧 Contact

- [GitHub Issues](https://github.com/coinbase-samples/core-java/issues)

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

The *Coinbase Java Core* sample library is free and open source and released under the [Apache License, Version 2.0](LICENSE).

Third-party dependency notices: [THIRD-PARTY-NOTICES](THIRD-PARTY-NOTICES).

The application and code are only available for demonstration purposes.

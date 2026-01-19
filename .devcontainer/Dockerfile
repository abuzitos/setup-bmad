FROM mcr.microsoft.com/devcontainers/java:1-21

# Instalar Maven se não estiver presente
RUN apt-get update && apt-get install -y \
    maven \
    && rm -rf /var/lib/apt/lists/*

# Configurar variáveis de ambiente
ENV JAVA_HOME=/usr/local/sdkman/candidates/java/current
ENV MAVEN_HOME=/usr/share/maven
ENV PATH="${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}"

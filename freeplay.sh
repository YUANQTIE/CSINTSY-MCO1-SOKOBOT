# Delete all .class files recursively
find . -name "*.class" -type f -delete

# Compile Driver.java with src as classpath
javac -cp src src/main/Driver.java

# Run program (pass first argument + "fp")
java -cp src main.Driver "$1" fp
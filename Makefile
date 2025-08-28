# Define variables for Maven commands
MVN = mvn

# Default goal to package and run the project
.PHONY: all
all: clean install run

# Clean the project
.PHONY: clean
clean:
	$(MVN) clean

# Run tests
.PHONY: test
test: clean
	$(MVN) test

# Install the project
.PHONY: install
install: clean
	$(MVN) install

# Package and execute the JAR file
.PHONY: run
run: package
	$(MVN) exec:java

# Just execute the JAR file
.PHONY: start
start:
	$(MVN) exec:java

# Package the project
.PHONY: package
package: clean
	$(MVN) package && echo "New Artifact Jar created in /target folder"

# Print help text
.PHONY: help
help:
	@echo "### Explanation:"
	@echo "---------------"
	@echo "- .PHONY targets are used to ensure that these targets are not confused with any files that might have the same name."
	@echo "- The 'all' target is defined as the default goal, which runs 'clean', 'install', and then 'exec-java'."
	@echo "- Each of the 'test', 'install', and 'package' targets first call the 'clean' target to clean the project before executing their specific Maven commands."
	@echo "------------"
	@echo "### Usage:"
	@echo "1. To clean the project: make clean"
	@echo "2. To run tests: make test"
	@echo "3. To install the project: make install"
	@echo "4. To package and execute the JAR file with a clean install first: make"
	@echo "5. To just execute the JAR file without clean ,install or package first: make start"
	@echo "---------------"
	@echo "@PWSS"
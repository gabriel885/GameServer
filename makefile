build:
	@mvn clean install
build-frontend:
	@cd frontend && mvn clean install
build-backend:
	@cd backend && mvn clean install
run:
	@cd backend && mvn spring-boot:run
buildrun: build run
clean:
	@cd backend && mvn clean
	@cd frontend && mvn clean && rm -rf dist

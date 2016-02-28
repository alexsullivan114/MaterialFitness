DROP TABLE IF EXISTS WorkoutSession;
CREATE TABLE WorkoutSession(_id INTEGER PRIMARY KEY, workoutSessionDate int);
DROP TABLE IF EXISTS ExerciseSession;
CREATE TABLE ExerciseSession(_id INTEGER PRIMARY KEY, exerciseId long, workoutSessionId int);
DROP TABLE IF EXISTS Exercise;
CREATE TABLE Exercise(_id INTEGER PRIMARY KEY, title VARCHAR, muscleGroup int);
DROP TABLE IF EXISTS WeightSet;
CREATE TABLE WeightSet(_id INTEGER PRIMARY KEY, weight int, reps int, exerciseSessionId int);
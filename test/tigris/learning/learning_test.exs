defmodule Tigris.LearningTest do
  use Tigris.DataCase

  alias Tigris.Learning

  describe "courses" do
    alias Tigris.Learning.Course

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def course_fixture(attrs \\ %{}) do
      {:ok, course} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Learning.create_course()

      course
    end

    test "list_courses/0 returns all courses" do
      course = course_fixture()
      assert Learning.list_courses() == [course]
    end

    test "get_course!/1 returns the course with given id" do
      course = course_fixture()
      assert Learning.get_course!(course.id) == course
    end

    test "create_course/1 with valid data creates a course" do
      assert {:ok, %Course{} = course} = Learning.create_course(@valid_attrs)
    end

    test "create_course/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Learning.create_course(@invalid_attrs)
    end

    test "update_course/2 with valid data updates the course" do
      course = course_fixture()
      assert {:ok, course} = Learning.update_course(course, @update_attrs)
      assert %Course{} = course
    end

    test "update_course/2 with invalid data returns error changeset" do
      course = course_fixture()
      assert {:error, %Ecto.Changeset{}} = Learning.update_course(course, @invalid_attrs)
      assert course == Learning.get_course!(course.id)
    end

    test "delete_course/1 deletes the course" do
      course = course_fixture()
      assert {:ok, %Course{}} = Learning.delete_course(course)
      assert_raise Ecto.NoResultsError, fn -> Learning.get_course!(course.id) end
    end

    test "change_course/1 returns a course changeset" do
      course = course_fixture()
      assert %Ecto.Changeset{} = Learning.change_course(course)
    end
  end

  describe "modules" do
    alias Tigris.Learning.Module

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def module_fixture(attrs \\ %{}) do
      {:ok, module} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Learning.create_module()

      module
    end

    test "list_modules/0 returns all modules" do
      module = module_fixture()
      assert Learning.list_modules() == [module]
    end

    test "get_module!/1 returns the module with given id" do
      module = module_fixture()
      assert Learning.get_module!(module.id) == module
    end

    test "create_module/1 with valid data creates a module" do
      assert {:ok, %Module{} = module} = Learning.create_module(@valid_attrs)
    end

    test "create_module/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Learning.create_module(@invalid_attrs)
    end

    test "update_module/2 with valid data updates the module" do
      module = module_fixture()
      assert {:ok, module} = Learning.update_module(module, @update_attrs)
      assert %Module{} = module
    end

    test "update_module/2 with invalid data returns error changeset" do
      module = module_fixture()
      assert {:error, %Ecto.Changeset{}} = Learning.update_module(module, @invalid_attrs)
      assert module == Learning.get_module!(module.id)
    end

    test "delete_module/1 deletes the module" do
      module = module_fixture()
      assert {:ok, %Module{}} = Learning.delete_module(module)
      assert_raise Ecto.NoResultsError, fn -> Learning.get_module!(module.id) end
    end

    test "change_module/1 returns a module changeset" do
      module = module_fixture()
      assert %Ecto.Changeset{} = Learning.change_module(module)
    end
  end

  describe "enrollments" do
    alias Tigris.Learning.Enrollment

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def enrollment_fixture(attrs \\ %{}) do
      {:ok, enrollment} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Learning.create_enrollment()

      enrollment
    end

    test "list_enrollments/0 returns all enrollments" do
      enrollment = enrollment_fixture()
      assert Learning.list_enrollments() == [enrollment]
    end

    test "get_enrollment!/1 returns the enrollment with given id" do
      enrollment = enrollment_fixture()
      assert Learning.get_enrollment!(enrollment.id) == enrollment
    end

    test "create_enrollment/1 with valid data creates a enrollment" do
      assert {:ok, %Enrollment{} = enrollment} = Learning.create_enrollment(@valid_attrs)
    end

    test "create_enrollment/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Learning.create_enrollment(@invalid_attrs)
    end

    test "update_enrollment/2 with valid data updates the enrollment" do
      enrollment = enrollment_fixture()
      assert {:ok, enrollment} = Learning.update_enrollment(enrollment, @update_attrs)
      assert %Enrollment{} = enrollment
    end

    test "update_enrollment/2 with invalid data returns error changeset" do
      enrollment = enrollment_fixture()
      assert {:error, %Ecto.Changeset{}} = Learning.update_enrollment(enrollment, @invalid_attrs)
      assert enrollment == Learning.get_enrollment!(enrollment.id)
    end

    test "delete_enrollment/1 deletes the enrollment" do
      enrollment = enrollment_fixture()
      assert {:ok, %Enrollment{}} = Learning.delete_enrollment(enrollment)
      assert_raise Ecto.NoResultsError, fn -> Learning.get_enrollment!(enrollment.id) end
    end

    test "change_enrollment/1 returns a enrollment changeset" do
      enrollment = enrollment_fixture()
      assert %Ecto.Changeset{} = Learning.change_enrollment(enrollment)
    end
  end

  describe "quizzes" do
    alias Tigris.Learning.Quiz

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def quiz_fixture(attrs \\ %{}) do
      {:ok, quiz} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Learning.create_quiz()

      quiz
    end

    test "list_quizzes/0 returns all quizzes" do
      quiz = quiz_fixture()
      assert Learning.list_quizzes() == [quiz]
    end

    test "get_quiz!/1 returns the quiz with given id" do
      quiz = quiz_fixture()
      assert Learning.get_quiz!(quiz.id) == quiz
    end

    test "create_quiz/1 with valid data creates a quiz" do
      assert {:ok, %Quiz{} = quiz} = Learning.create_quiz(@valid_attrs)
    end

    test "create_quiz/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Learning.create_quiz(@invalid_attrs)
    end

    test "update_quiz/2 with valid data updates the quiz" do
      quiz = quiz_fixture()
      assert {:ok, quiz} = Learning.update_quiz(quiz, @update_attrs)
      assert %Quiz{} = quiz
    end

    test "update_quiz/2 with invalid data returns error changeset" do
      quiz = quiz_fixture()
      assert {:error, %Ecto.Changeset{}} = Learning.update_quiz(quiz, @invalid_attrs)
      assert quiz == Learning.get_quiz!(quiz.id)
    end

    test "delete_quiz/1 deletes the quiz" do
      quiz = quiz_fixture()
      assert {:ok, %Quiz{}} = Learning.delete_quiz(quiz)
      assert_raise Ecto.NoResultsError, fn -> Learning.get_quiz!(quiz.id) end
    end

    test "change_quiz/1 returns a quiz changeset" do
      quiz = quiz_fixture()
      assert %Ecto.Changeset{} = Learning.change_quiz(quiz)
    end
  end

  describe "tests" do
    alias Tigris.Learning.Test

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def test_fixture(attrs \\ %{}) do
      {:ok, test} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Learning.create_test()

      test
    end

    test "list_tests/0 returns all tests" do
      test = test_fixture()
      assert Learning.list_tests() == [test]
    end

    test "get_test!/1 returns the test with given id" do
      test = test_fixture()
      assert Learning.get_test!(test.id) == test
    end

    test "create_test/1 with valid data creates a test" do
      assert {:ok, %Test{} = test} = Learning.create_test(@valid_attrs)
    end

    test "create_test/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Learning.create_test(@invalid_attrs)
    end

    test "update_test/2 with valid data updates the test" do
      test = test_fixture()
      assert {:ok, test} = Learning.update_test(test, @update_attrs)
      assert %Test{} = test
    end

    test "update_test/2 with invalid data returns error changeset" do
      test = test_fixture()
      assert {:error, %Ecto.Changeset{}} = Learning.update_test(test, @invalid_attrs)
      assert test == Learning.get_test!(test.id)
    end

    test "delete_test/1 deletes the test" do
      test = test_fixture()
      assert {:ok, %Test{}} = Learning.delete_test(test)
      assert_raise Ecto.NoResultsError, fn -> Learning.get_test!(test.id) end
    end

    test "change_test/1 returns a test changeset" do
      test = test_fixture()
      assert %Ecto.Changeset{} = Learning.change_test(test)
    end
  end

  describe "quiz_submissions" do
    alias Tigris.Learning.QuizSubmission

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def quiz_submission_fixture(attrs \\ %{}) do
      {:ok, quiz_submission} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Learning.create_quiz_submission()

      quiz_submission
    end

    test "list_quiz_submissions/0 returns all quiz_submissions" do
      quiz_submission = quiz_submission_fixture()
      assert Learning.list_quiz_submissions() == [quiz_submission]
    end

    test "get_quiz_submission!/1 returns the quiz_submission with given id" do
      quiz_submission = quiz_submission_fixture()
      assert Learning.get_quiz_submission!(quiz_submission.id) == quiz_submission
    end

    test "create_quiz_submission/1 with valid data creates a quiz_submission" do
      assert {:ok, %QuizSubmission{} = quiz_submission} = Learning.create_quiz_submission(@valid_attrs)
    end

    test "create_quiz_submission/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Learning.create_quiz_submission(@invalid_attrs)
    end

    test "update_quiz_submission/2 with valid data updates the quiz_submission" do
      quiz_submission = quiz_submission_fixture()
      assert {:ok, quiz_submission} = Learning.update_quiz_submission(quiz_submission, @update_attrs)
      assert %QuizSubmission{} = quiz_submission
    end

    test "update_quiz_submission/2 with invalid data returns error changeset" do
      quiz_submission = quiz_submission_fixture()
      assert {:error, %Ecto.Changeset{}} = Learning.update_quiz_submission(quiz_submission, @invalid_attrs)
      assert quiz_submission == Learning.get_quiz_submission!(quiz_submission.id)
    end

    test "delete_quiz_submission/1 deletes the quiz_submission" do
      quiz_submission = quiz_submission_fixture()
      assert {:ok, %QuizSubmission{}} = Learning.delete_quiz_submission(quiz_submission)
      assert_raise Ecto.NoResultsError, fn -> Learning.get_quiz_submission!(quiz_submission.id) end
    end

    test "change_quiz_submission/1 returns a quiz_submission changeset" do
      quiz_submission = quiz_submission_fixture()
      assert %Ecto.Changeset{} = Learning.change_quiz_submission(quiz_submission)
    end
  end

  describe "test_submissions" do
    alias Tigris.Learning.TestSubmission

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def test_submission_fixture(attrs \\ %{}) do
      {:ok, test_submission} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Learning.create_test_submission()

      test_submission
    end

    test "list_test_submissions/0 returns all test_submissions" do
      test_submission = test_submission_fixture()
      assert Learning.list_test_submissions() == [test_submission]
    end

    test "get_test_submission!/1 returns the test_submission with given id" do
      test_submission = test_submission_fixture()
      assert Learning.get_test_submission!(test_submission.id) == test_submission
    end

    test "create_test_submission/1 with valid data creates a test_submission" do
      assert {:ok, %TestSubmission{} = test_submission} = Learning.create_test_submission(@valid_attrs)
    end

    test "create_test_submission/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Learning.create_test_submission(@invalid_attrs)
    end

    test "update_test_submission/2 with valid data updates the test_submission" do
      test_submission = test_submission_fixture()
      assert {:ok, test_submission} = Learning.update_test_submission(test_submission, @update_attrs)
      assert %TestSubmission{} = test_submission
    end

    test "update_test_submission/2 with invalid data returns error changeset" do
      test_submission = test_submission_fixture()
      assert {:error, %Ecto.Changeset{}} = Learning.update_test_submission(test_submission, @invalid_attrs)
      assert test_submission == Learning.get_test_submission!(test_submission.id)
    end

    test "delete_test_submission/1 deletes the test_submission" do
      test_submission = test_submission_fixture()
      assert {:ok, %TestSubmission{}} = Learning.delete_test_submission(test_submission)
      assert_raise Ecto.NoResultsError, fn -> Learning.get_test_submission!(test_submission.id) end
    end

    test "change_test_submission/1 returns a test_submission changeset" do
      test_submission = test_submission_fixture()
      assert %Ecto.Changeset{} = Learning.change_test_submission(test_submission)
    end
  end

  describe "tags" do
    alias Tigris.Learning.Tag

    @valid_attrs %{tag: "some tag"}
    @update_attrs %{tag: "some updated tag"}
    @invalid_attrs %{tag: nil}

    def tag_fixture(attrs \\ %{}) do
      {:ok, tag} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Learning.create_tag()

      tag
    end

    test "list_tags/0 returns all tags" do
      tag = tag_fixture()
      assert Learning.list_tags() == [tag]
    end

    test "get_tag!/1 returns the tag with given id" do
      tag = tag_fixture()
      assert Learning.get_tag!(tag.id) == tag
    end

    test "create_tag/1 with valid data creates a tag" do
      assert {:ok, %Tag{} = tag} = Learning.create_tag(@valid_attrs)
      assert tag.tag == "some tag"
    end

    test "create_tag/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Learning.create_tag(@invalid_attrs)
    end

    test "update_tag/2 with valid data updates the tag" do
      tag = tag_fixture()
      assert {:ok, tag} = Learning.update_tag(tag, @update_attrs)
      assert %Tag{} = tag
      assert tag.tag == "some updated tag"
    end

    test "update_tag/2 with invalid data returns error changeset" do
      tag = tag_fixture()
      assert {:error, %Ecto.Changeset{}} = Learning.update_tag(tag, @invalid_attrs)
      assert tag == Learning.get_tag!(tag.id)
    end

    test "delete_tag/1 deletes the tag" do
      tag = tag_fixture()
      assert {:ok, %Tag{}} = Learning.delete_tag(tag)
      assert_raise Ecto.NoResultsError, fn -> Learning.get_tag!(tag.id) end
    end

    test "change_tag/1 returns a tag changeset" do
      tag = tag_fixture()
      assert %Ecto.Changeset{} = Learning.change_tag(tag)
    end
  end
end

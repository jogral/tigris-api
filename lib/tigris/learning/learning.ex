defmodule Tigris.Learning do
  @moduledoc """
  The Learning context.
  """

  import Ecto.Query, warn: false
  alias Tigris.Repo

  alias Tigris.Learning.Course

  @doc """
  Returns the list of courses.

  ## Examples

      iex> list_courses()
      [%Course{}, ...]

  """
  def list_courses do
    Repo.all(Course)
  end

  @doc """
  Gets a single course.

  Raises `Ecto.NoResultsError` if the Course does not exist.

  ## Examples

      iex> get_course!(123)
      %Course{}

      iex> get_course!(456)
      ** (Ecto.NoResultsError)

  """
  def get_course!(id), do: Repo.get!(Course, id)

  @doc """
  Creates a course.

  ## Examples

      iex> create_course(%{field: value})
      {:ok, %Course{}}

      iex> create_course(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_course(attrs \\ %{}) do
    %Course{}
    |> Course.changeset(attrs)
    |> Repo.insert()
  end

  @doc """
  Updates a course.

  ## Examples

      iex> update_course(course, %{field: new_value})
      {:ok, %Course{}}

      iex> update_course(course, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_course(%Course{} = course, attrs) do
    course
    |> Course.changeset(attrs)
    |> Repo.update()
  end

  @doc """
  Deletes a Course.

  ## Examples

      iex> delete_course(course)
      {:ok, %Course{}}

      iex> delete_course(course)
      {:error, %Ecto.Changeset{}}

  """
  def delete_course(%Course{} = course) do
    Repo.delete(course)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking course changes.

  ## Examples

      iex> change_course(course)
      %Ecto.Changeset{source: %Course{}}

  """
  def change_course(%Course{} = course) do
    Course.changeset(course, %{})
  end

  alias Tigris.Learning.Module

  @doc """
  Returns the list of modules.

  ## Examples

      iex> list_modules()
      [%Module{}, ...]

  """
  def list_modules do
    Repo.all(Module)
  end

  @doc """
  Gets a single module.

  Raises `Ecto.NoResultsError` if the Module does not exist.

  ## Examples

      iex> get_module!(123)
      %Module{}

      iex> get_module!(456)
      ** (Ecto.NoResultsError)

  """
  def get_module!(id), do: Repo.get!(Module, id)

  @doc """
  Creates a module.

  ## Examples

      iex> create_module(%{field: value})
      {:ok, %Module{}}

      iex> create_module(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_module(attrs \\ %{}) do
    %Module{}
    |> Module.changeset(attrs)
    |> Repo.insert()
  end

  @doc """
  Updates a module.

  ## Examples

      iex> update_module(module, %{field: new_value})
      {:ok, %Module{}}

      iex> update_module(module, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_module(%Module{} = module, attrs) do
    module
    |> Module.changeset(attrs)
    |> Repo.update()
  end

  @doc """
  Deletes a Module.

  ## Examples

      iex> delete_module(module)
      {:ok, %Module{}}

      iex> delete_module(module)
      {:error, %Ecto.Changeset{}}

  """
  def delete_module(%Module{} = module) do
    Repo.delete(module)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking module changes.

  ## Examples

      iex> change_module(module)
      %Ecto.Changeset{source: %Module{}}

  """
  def change_module(%Module{} = module) do
    Module.changeset(module, %{})
  end

  alias Tigris.Learning.Enrollment

  @doc """
  Returns the list of enrollments.

  ## Examples

      iex> list_enrollments()
      [%Enrollment{}, ...]

  """
  def list_enrollments do
    Repo.all(Enrollment)
  end

  @doc """
  Gets a single enrollment.

  Raises `Ecto.NoResultsError` if the Enrollment does not exist.

  ## Examples

      iex> get_enrollment!(123)
      %Enrollment{}

      iex> get_enrollment!(456)
      ** (Ecto.NoResultsError)

  """
  def get_enrollment!(id), do: Repo.get!(Enrollment, id)

  @doc """
  Creates a enrollment.

  ## Examples

      iex> create_enrollment(%{field: value})
      {:ok, %Enrollment{}}

      iex> create_enrollment(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_enrollment(attrs \\ %{}) do
    %Enrollment{}
    |> Enrollment.changeset(attrs)
    |> Repo.insert()
  end

  @doc """
  Updates a enrollment.

  ## Examples

      iex> update_enrollment(enrollment, %{field: new_value})
      {:ok, %Enrollment{}}

      iex> update_enrollment(enrollment, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_enrollment(%Enrollment{} = enrollment, attrs) do
    enrollment
    |> Enrollment.changeset(attrs)
    |> Repo.update()
  end

  @doc """
  Deletes a Enrollment.

  ## Examples

      iex> delete_enrollment(enrollment)
      {:ok, %Enrollment{}}

      iex> delete_enrollment(enrollment)
      {:error, %Ecto.Changeset{}}

  """
  def delete_enrollment(%Enrollment{} = enrollment) do
    Repo.delete(enrollment)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking enrollment changes.

  ## Examples

      iex> change_enrollment(enrollment)
      %Ecto.Changeset{source: %Enrollment{}}

  """
  def change_enrollment(%Enrollment{} = enrollment) do
    Enrollment.changeset(enrollment, %{})
  end

  alias Tigris.Learning.Quiz

  @doc """
  Returns the list of quizzes.

  ## Examples

      iex> list_quizzes()
      [%Quiz{}, ...]

  """
  def list_quizzes do
    Repo.all(Quiz)
  end

  @doc """
  Gets a single quiz.

  Raises `Ecto.NoResultsError` if the Quiz does not exist.

  ## Examples

      iex> get_quiz!(123)
      %Quiz{}

      iex> get_quiz!(456)
      ** (Ecto.NoResultsError)

  """
  def get_quiz!(id), do: Repo.get!(Quiz, id)

  @doc """
  Creates a quiz.

  ## Examples

      iex> create_quiz(%{field: value})
      {:ok, %Quiz{}}

      iex> create_quiz(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_quiz(attrs \\ %{}) do
    %Quiz{}
    |> Quiz.changeset(attrs)
    |> Repo.insert()
  end

  @doc """
  Updates a quiz.

  ## Examples

      iex> update_quiz(quiz, %{field: new_value})
      {:ok, %Quiz{}}

      iex> update_quiz(quiz, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_quiz(%Quiz{} = quiz, attrs) do
    quiz
    |> Quiz.changeset(attrs)
    |> Repo.update()
  end

  @doc """
  Deletes a Quiz.

  ## Examples

      iex> delete_quiz(quiz)
      {:ok, %Quiz{}}

      iex> delete_quiz(quiz)
      {:error, %Ecto.Changeset{}}

  """
  def delete_quiz(%Quiz{} = quiz) do
    Repo.delete(quiz)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking quiz changes.

  ## Examples

      iex> change_quiz(quiz)
      %Ecto.Changeset{source: %Quiz{}}

  """
  def change_quiz(%Quiz{} = quiz) do
    Quiz.changeset(quiz, %{})
  end

  alias Tigris.Learning.Test

  @doc """
  Returns the list of tests.

  ## Examples

      iex> list_tests()
      [%Test{}, ...]

  """
  def list_tests do
    Repo.all(Test)
  end

  @doc """
  Gets a single test.

  Raises `Ecto.NoResultsError` if the Test does not exist.

  ## Examples

      iex> get_test!(123)
      %Test{}

      iex> get_test!(456)
      ** (Ecto.NoResultsError)

  """
  def get_test!(id), do: Repo.get!(Test, id)

  @doc """
  Creates a test.

  ## Examples

      iex> create_test(%{field: value})
      {:ok, %Test{}}

      iex> create_test(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_test(attrs \\ %{}) do
    %Test{}
    |> Test.changeset(attrs)
    |> Repo.insert()
  end

  @doc """
  Updates a test.

  ## Examples

      iex> update_test(test, %{field: new_value})
      {:ok, %Test{}}

      iex> update_test(test, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_test(%Test{} = test, attrs) do
    test
    |> Test.changeset(attrs)
    |> Repo.update()
  end

  @doc """
  Deletes a Test.

  ## Examples

      iex> delete_test(test)
      {:ok, %Test{}}

      iex> delete_test(test)
      {:error, %Ecto.Changeset{}}

  """
  def delete_test(%Test{} = test) do
    Repo.delete(test)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking test changes.

  ## Examples

      iex> change_test(test)
      %Ecto.Changeset{source: %Test{}}

  """
  def change_test(%Test{} = test) do
    Test.changeset(test, %{})
  end

  alias Tigris.Learning.QuizSubmission

  @doc """
  Returns the list of quiz_submissions.

  ## Examples

      iex> list_quiz_submissions()
      [%QuizSubmission{}, ...]

  """
  def list_quiz_submissions do
    Repo.all(QuizSubmission)
  end

  @doc """
  Gets a single quiz_submission.

  Raises `Ecto.NoResultsError` if the Quiz submission does not exist.

  ## Examples

      iex> get_quiz_submission!(123)
      %QuizSubmission{}

      iex> get_quiz_submission!(456)
      ** (Ecto.NoResultsError)

  """
  def get_quiz_submission!(id), do: Repo.get!(QuizSubmission, id)

  @doc """
  Creates a quiz_submission.

  ## Examples

      iex> create_quiz_submission(%{field: value})
      {:ok, %QuizSubmission{}}

      iex> create_quiz_submission(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_quiz_submission(attrs \\ %{}) do
    %QuizSubmission{}
    |> QuizSubmission.changeset(attrs)
    |> Repo.insert()
  end

  @doc """
  Updates a quiz_submission.

  ## Examples

      iex> update_quiz_submission(quiz_submission, %{field: new_value})
      {:ok, %QuizSubmission{}}

      iex> update_quiz_submission(quiz_submission, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_quiz_submission(%QuizSubmission{} = quiz_submission, attrs) do
    quiz_submission
    |> QuizSubmission.changeset(attrs)
    |> Repo.update()
  end

  @doc """
  Deletes a QuizSubmission.

  ## Examples

      iex> delete_quiz_submission(quiz_submission)
      {:ok, %QuizSubmission{}}

      iex> delete_quiz_submission(quiz_submission)
      {:error, %Ecto.Changeset{}}

  """
  def delete_quiz_submission(%QuizSubmission{} = quiz_submission) do
    Repo.delete(quiz_submission)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking quiz_submission changes.

  ## Examples

      iex> change_quiz_submission(quiz_submission)
      %Ecto.Changeset{source: %QuizSubmission{}}

  """
  def change_quiz_submission(%QuizSubmission{} = quiz_submission) do
    QuizSubmission.changeset(quiz_submission, %{})
  end

  alias Tigris.Learning.TestSubmission

  @doc """
  Returns the list of test_submissions.

  ## Examples

      iex> list_test_submissions()
      [%TestSubmission{}, ...]

  """
  def list_test_submissions do
    Repo.all(TestSubmission)
  end

  @doc """
  Gets a single test_submission.

  Raises `Ecto.NoResultsError` if the Test submission does not exist.

  ## Examples

      iex> get_test_submission!(123)
      %TestSubmission{}

      iex> get_test_submission!(456)
      ** (Ecto.NoResultsError)

  """
  def get_test_submission!(id), do: Repo.get!(TestSubmission, id)

  @doc """
  Creates a test_submission.

  ## Examples

      iex> create_test_submission(%{field: value})
      {:ok, %TestSubmission{}}

      iex> create_test_submission(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_test_submission(attrs \\ %{}) do
    %TestSubmission{}
    |> TestSubmission.changeset(attrs)
    |> Repo.insert()
  end

  @doc """
  Updates a test_submission.

  ## Examples

      iex> update_test_submission(test_submission, %{field: new_value})
      {:ok, %TestSubmission{}}

      iex> update_test_submission(test_submission, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_test_submission(%TestSubmission{} = test_submission, attrs) do
    test_submission
    |> TestSubmission.changeset(attrs)
    |> Repo.update()
  end

  @doc """
  Deletes a TestSubmission.

  ## Examples

      iex> delete_test_submission(test_submission)
      {:ok, %TestSubmission{}}

      iex> delete_test_submission(test_submission)
      {:error, %Ecto.Changeset{}}

  """
  def delete_test_submission(%TestSubmission{} = test_submission) do
    Repo.delete(test_submission)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking test_submission changes.

  ## Examples

      iex> change_test_submission(test_submission)
      %Ecto.Changeset{source: %TestSubmission{}}

  """
  def change_test_submission(%TestSubmission{} = test_submission) do
    TestSubmission.changeset(test_submission, %{})
  end

  alias Tigris.Learning.Tag

  @doc """
  Returns the list of tags.

  ## Examples

      iex> list_tags()
      [%Tag{}, ...]

  """
  def list_tags do
    Repo.all(Tag)
  end

  @doc """
  Gets a single tag.

  Raises `Ecto.NoResultsError` if the Tag does not exist.

  ## Examples

      iex> get_tag!(123)
      %Tag{}

      iex> get_tag!(456)
      ** (Ecto.NoResultsError)

  """
  def get_tag!(id), do: Repo.get!(Tag, id)

  @doc """
  Creates a tag.

  ## Examples

      iex> create_tag(%{field: value})
      {:ok, %Tag{}}

      iex> create_tag(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_tag(attrs \\ %{}) do
    %Tag{}
    |> Tag.changeset(attrs)
    |> Repo.insert()
  end

  @doc """
  Updates a tag.

  ## Examples

      iex> update_tag(tag, %{field: new_value})
      {:ok, %Tag{}}

      iex> update_tag(tag, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_tag(%Tag{} = tag, attrs) do
    tag
    |> Tag.changeset(attrs)
    |> Repo.update()
  end

  @doc """
  Deletes a Tag.

  ## Examples

      iex> delete_tag(tag)
      {:ok, %Tag{}}

      iex> delete_tag(tag)
      {:error, %Ecto.Changeset{}}

  """
  def delete_tag(%Tag{} = tag) do
    Repo.delete(tag)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking tag changes.

  ## Examples

      iex> change_tag(tag)
      %Ecto.Changeset{source: %Tag{}}

  """
  def change_tag(%Tag{} = tag) do
    Tag.changeset(tag, %{})
  end
end

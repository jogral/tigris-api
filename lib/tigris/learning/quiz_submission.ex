defmodule Tigris.Learning.QuizSubmission do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Learning.QuizSubmission


  schema "quiz_submissions" do
    field :grade, :float
    field :submission, :map
    belongs_to :enrollment, Tigris.Learning.Enrollment

    timestamps()
  end

  @doc false
  def changeset(%QuizSubmission{} = quiz_submission, attrs) do
    quiz_submission
    |> cast(attrs, [:submission, :grade])
    |> validate_required([:submission, :grade])
  end
end

defmodule Tigris.Learning.TestSubmission do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Learning.TestSubmission


  schema "test_submissions" do
    field :grade, :float
    field :submission, :map
    belongs_to :enrollment, Tigris.Learning.Enrollment

    timestamps()
  end

  @doc false
  def changeset(%TestSubmission{} = test_submission, attrs) do
    test_submission
    |> cast(attrs, [:submission, :grade])
    |> validate_required([:submission, :grade])
  end
end

defmodule Tigris.Learning.Quiz do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Learning.Quiz


  schema "quizzes" do
    field :status, :integer, default: 0
    field :data, :map
    belongs_to :module, Tigris.Learning.Module

    timestamps()
  end

  @doc false
  def changeset(%Quiz{} = quiz, attrs) do
    quiz
    |> cast(attrs, [:status, :data])
    |> validate_required([])
    |> validate_number(:status, greater_than_or_equal_to: -1, less_than_or_equal_to: 1)
  end
end

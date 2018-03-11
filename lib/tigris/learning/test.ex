defmodule Tigris.Learning.Test do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Learning.Test


  schema "tests" do
    field :status, :integer, default: 0
    field :data, :map
    belongs_to :course, Tigris.Learning.Course

    timestamps()
  end

  @doc false
  def changeset(%Test{} = test, attrs) do
    test
    |> cast(attrs, [:status, :data])
    |> validate_required([])
    |> validate_number(:status, greater_than_or_equal_to: -1, less_than_or_equal_to: 1)
  end
end

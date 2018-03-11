defmodule Tigris.Learning.Enrollment do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Learning.Enrollment


  schema "enrollments" do
    field :progress, :map
    field :registered, :naive_datetime
    field :completed, :naive_datetime
    field :enrolled, :boolean, default: true
    belongs_to :student, Tigris.Users.User
    belongs_to :course, Tigris.Learning.Course

    timestamps()
  end

  @doc false
  def changeset(%Enrollment{} = enrollment, attrs) do
    enrollment
    |> cast(attrs, [:progress, :registered, :completed, :enrolled])
    |> validate_required([:registered])
  end
end

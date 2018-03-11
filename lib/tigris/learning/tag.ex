defmodule Tigris.Learning.Tag do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Learning.Tag


  schema "tags" do
    field :tag, :string
    # belongs_to :course, Tigris.Learning.Course
    timestamps()
  end

  @doc false
  def changeset(%Tag{} = tag, attrs) do
    tag
    |> cast(attrs, [:tag])
    |> validate_required([:tag])
  end
end

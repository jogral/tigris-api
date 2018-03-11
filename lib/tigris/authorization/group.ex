defmodule Tigris.Authorization.Group do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Authorization.Group


  schema "groups" do
    field :name, :string
    field :description, :string
    field :active, :boolean, default: true
    field :deletable, :boolean, default: true
    has_many :permissions, Tigris.Authorization.Permission
    timestamps()
  end

  @doc false
  def changeset(%Group{} = group, attrs) do
    group
    |> cast(attrs, [:name, :description, :active, :deletable])
    |> validate_required([])
  end
end

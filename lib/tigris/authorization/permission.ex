defmodule Tigris.Authorization.Permission do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Authorization.Permission


  schema "permissions" do
    field :name, :string
    field :description, :string
    field :active, :boolean, default: true
    timestamps()
  end

  @required_fields ~w(active)
  @optional_fields ~w(name description)

  @doc false
  def changeset(%Permission{} = permission, attrs) do
    permission
    |> cast(attrs, [:name, :description, :active])
    |> validate_required([])
  end
end

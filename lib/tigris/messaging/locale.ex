defmodule Tigris.Messaging.Locale do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Messaging.Locale


  schema "locales" do
    field :locale, :string
    field :description, :string

    timestamps()
  end

  @doc false
  def changeset(%Locale{} = locale, attrs) do
    locale
    |> cast(attrs, [:locale, :description])
    |> validate_required([:locale])
    |> unique_constraint(:locale)
  end
end

defmodule Tigris.Messaging.Notification do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Messaging.Notification


  schema "notifications" do
    field :title, :string
    field :message, :string
    field :available, :boolean, default: true
    field :sent, :naive_datetime
    belongs_to :sender, Tigris.Users.User
    belongs_to :recipient, Tigris.Users.User

    timestamps()
  end

  @doc false
  def changeset(%Notification{} = notification, attrs) do
    notification
    |> cast(attrs, [:title, :available])
    |> validate_required([])
  end
end

defmodule Tigris.Learning.Module.TitleSlug do
  use EctoAutoslugField.Slug, from: :title, to: :slug
end

defmodule Tigris.Learning.Module do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Learning.Module


  schema "modules" do
    field :title, :string
    field :slug, TitleSlug.Type
    field :content, :string
    field :description, :string
    field :order_index, :integer
    field :active, :boolean, default: true
    belongs_to :creator, Tigris.Users.User
    belongs_to :course, Tigris.Learning.Course
    has_one :quiz, Tigris.Learning.Quiz

    timestamps()
  end

  @doc false
  def changeset(%Module{} = module, attrs) do
    module
    |> cast(attrs, [:title, :content, :description, :order_index])
    |> validate_required([:title, :content])
    |> unique_constraint([:course, :order_index], Tigris.Repo, message: "Order must be unique.")
  end
end

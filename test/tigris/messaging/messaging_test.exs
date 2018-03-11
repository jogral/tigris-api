defmodule Tigris.MessagingTest do
  use Tigris.DataCase

  alias Tigris.Messaging

  describe "notifications" do
    alias Tigris.Messaging.Notification

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def notification_fixture(attrs \\ %{}) do
      {:ok, notification} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Messaging.create_notification()

      notification
    end

    test "list_notifications/0 returns all notifications" do
      notification = notification_fixture()
      assert Messaging.list_notifications() == [notification]
    end

    test "get_notification!/1 returns the notification with given id" do
      notification = notification_fixture()
      assert Messaging.get_notification!(notification.id) == notification
    end

    test "create_notification/1 with valid data creates a notification" do
      assert {:ok, %Notification{} = notification} = Messaging.create_notification(@valid_attrs)
    end

    test "create_notification/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Messaging.create_notification(@invalid_attrs)
    end

    test "update_notification/2 with valid data updates the notification" do
      notification = notification_fixture()
      assert {:ok, notification} = Messaging.update_notification(notification, @update_attrs)
      assert %Notification{} = notification
    end

    test "update_notification/2 with invalid data returns error changeset" do
      notification = notification_fixture()
      assert {:error, %Ecto.Changeset{}} = Messaging.update_notification(notification, @invalid_attrs)
      assert notification == Messaging.get_notification!(notification.id)
    end

    test "delete_notification/1 deletes the notification" do
      notification = notification_fixture()
      assert {:ok, %Notification{}} = Messaging.delete_notification(notification)
      assert_raise Ecto.NoResultsError, fn -> Messaging.get_notification!(notification.id) end
    end

    test "change_notification/1 returns a notification changeset" do
      notification = notification_fixture()
      assert %Ecto.Changeset{} = Messaging.change_notification(notification)
    end
  end

  describe "locales" do
    alias Tigris.Messaging.Locale

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def locale_fixture(attrs \\ %{}) do
      {:ok, locale} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Messaging.create_locale()

      locale
    end

    test "list_locales/0 returns all locales" do
      locale = locale_fixture()
      assert Messaging.list_locales() == [locale]
    end

    test "get_locale!/1 returns the locale with given id" do
      locale = locale_fixture()
      assert Messaging.get_locale!(locale.id) == locale
    end

    test "create_locale/1 with valid data creates a locale" do
      assert {:ok, %Locale{} = locale} = Messaging.create_locale(@valid_attrs)
    end

    test "create_locale/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Messaging.create_locale(@invalid_attrs)
    end

    test "update_locale/2 with valid data updates the locale" do
      locale = locale_fixture()
      assert {:ok, locale} = Messaging.update_locale(locale, @update_attrs)
      assert %Locale{} = locale
    end

    test "update_locale/2 with invalid data returns error changeset" do
      locale = locale_fixture()
      assert {:error, %Ecto.Changeset{}} = Messaging.update_locale(locale, @invalid_attrs)
      assert locale == Messaging.get_locale!(locale.id)
    end

    test "delete_locale/1 deletes the locale" do
      locale = locale_fixture()
      assert {:ok, %Locale{}} = Messaging.delete_locale(locale)
      assert_raise Ecto.NoResultsError, fn -> Messaging.get_locale!(locale.id) end
    end

    test "change_locale/1 returns a locale changeset" do
      locale = locale_fixture()
      assert %Ecto.Changeset{} = Messaging.change_locale(locale)
    end
  end
end

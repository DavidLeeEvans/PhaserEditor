namespace PhaserEditor2D {

    function get_property(name: string, defaultValue?: any) {
        return function (data: any) {
            var value = data[name];
            if (value == undefined) {
                return defaultValue;
            }
            return value;
        };
    }

    function set_property(name: string) {
        return function (data: any, value: any) {
            data[name] = value;
        };
    }

    export const GameObjectEditorComponent = {
        get_gameObjectEditorTransparency : get_property("gameObjectEditorTransparency", 1),

        updateObject: function (obj: Phaser.GameObjects.Components.Alpha, data: any) {
            obj.alpha = this.get_gameObjectEditorTransparency(data);
        }
    };


    export const TransformComponent = {
        get_x: get_property("x", 0),
        get_y: get_property("y", 0),
        get_scaleX: get_property("scaleX", 1),
        get_scaleY: get_property("scaleY", 1),
        get_angle: get_property("angle", 0),

        updateObject: function (obj: Phaser.GameObjects.Components.Transform, data: any) {
            obj.x = this.get_x(data);
            obj.y = this.get_y(data);
            obj.scaleX = this.get_scaleX(data);
            obj.scaleY = this.get_scaleY(data);
            obj.angle = this.get_angle(data);
        }
    };

    export const OriginComponent = {
        get_originX: get_property("originX", 0.5),
        get_originY: get_property("originY", 0.5),

        updateObject: function (obj: Phaser.GameObjects.Components.Origin, data: any) {
            obj.setOrigin(this.get_originX(data), this.get_originY(data));
        }
    };

    export const TextureComponent = {
        get_textureKey: get_property("textureKey"),
        get_textureFrame: get_property("textureFrame")
    };

    export const TileSpriteComponent = {
        get_tilePositionX: get_property("tilePositionX", 0),
        get_tilePositionY: get_property("tilePositionY", 0),
        get_tileScaleX: get_property("tileScaleX", 1),
        get_tileScaleY: get_property("tileScaleY", 1),
        get_width: get_property("width", -1),
        get_height: get_property("height", -1),

        updateObject: function (obj: Phaser.GameObjects.TileSprite, data: any) {
            obj.setTilePosition(this.get_tilePositionX(data), this.get_tilePositionY(data));
            obj.setTileScale(this.get_tileScaleX(data), this.get_tileScaleY(data));
        }
    };

    export const FlipComponent = {
        get_flipX: get_property("flipX", false),
        get_flipY: get_property("flipY", false),

        updateObject: function (obj: Phaser.GameObjects.Components.Flip, data: any) {
            obj.flipX = this.get_flipX(data);
            obj.flipY = this.get_flipY(data);
        }
    };

    export const BitmapTextComponent = {
        get_fontSize: get_property("fontSize", 0),
        get_align: get_property("align", 0),
        get_letterSpacing: get_property("letterSpacing", 0),
        get_fontAssetKey: get_property("fontAssetKey"),

        // the BitmapText object has a default origin of 0, 0
        get_originX: get_property("originX", 0),
        get_originY: get_property("originY", 0),

        updateObject: function (obj: Phaser.GameObjects.BitmapText, data: any) {
            obj.text = TextualComponent.get_text(data);
            obj.fontSize = this.get_fontSize(data);
            obj.align = this.get_align(data);
            obj.letterSpacing = this.get_letterSpacing(data);
            obj.setOrigin(this.get_originX(data), this.get_originY(data));
        }
    };

    export const TextualComponent = {
        get_text: get_property("text", "")
    };

    export const ScenePropertiesComponent = {
        get_snapEnabled: get_property("snapEnabled", false),
        get_snapWidth: get_property("snapWidth", 16),
        get_snapHeight: get_property("snapHeight", 16),
        get_backgroundColor: get_property("backgroundColor", "192,192,182"),
        get_foregroundColor: get_property("foregroundColor", "255,255,255"),
        get_borderX: get_property("borderX", 0),
        get_borderY: get_property("borderY", 0),
        get_borderWidth: get_property("borderWidth", 800),
        get_borderHeight: get_property("borderHeight", 600),
    };
}
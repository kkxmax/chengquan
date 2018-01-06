//
//  MSBrowseImageView.h
//  KnighteamFrame
//
//  Created by wenxx on 16/5/4.
//  Copyright © 2016年 wenxx. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^RemoveAction)();

@interface MSBrowseImageView : UIView

@property (nonatomic, copy) RemoveAction            removeView;

@property (nonatomic, assign) NSInteger                 currentIdx;

- (void)setBigImageArray:(NSArray *)array withCurrentIndex:(NSInteger)index;

- (void)setRemoveView:(RemoveAction)removeView;
@end

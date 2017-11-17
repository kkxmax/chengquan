//
//  ChoiceCategoryViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/12/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol ChoiceCategoryViewDelegate<NSObject>
@optional

- (void)hideChoiceCategoryView:(NSDictionary *)categoryDic;
@end

@interface ChoiceCategoryViewController : UIViewController
{
    id<ChoiceCategoryViewDelegate> delegate;
}
@property (nonatomic, retain) id<ChoiceCategoryViewDelegate> delegate;
@property (nonatomic) NSInteger categoryType;
@property (nonatomic, retain) NSString *categoryName;
@property (nonatomic, retain) NSString *pleixingID;
@property (nonatomic, retain) NSDictionary *selectedCategory;

@property (nonatomic, weak) IBOutlet UILabel *titleLabel;

- (void)getData;
@end

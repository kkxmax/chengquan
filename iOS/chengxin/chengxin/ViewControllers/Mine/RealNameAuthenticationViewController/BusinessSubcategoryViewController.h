//
//  BusinessSubcategoryViewController.h
//  chengxin
//
//  Created by common on 4/12/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol BusinessSubcategoryViewControllerDelegate <NSObject>

-(void)businessSelected:(NSArray*)aryBusiness;

@end
@interface BusinessSubcategoryViewController : UIViewController

@property id<BusinessSubcategoryViewControllerDelegate> delegate;
@property (nonatomic, retain) IBOutlet UIButton* btnBusiness1;
@property (nonatomic, retain) IBOutlet UIButton* btnBusiness2;
@property (nonatomic, retain) IBOutlet UIButton* btnBusiness3;
@property (nonatomic, retain) IBOutlet UIButton* btnBusiness4;
@property (nonatomic, retain) IBOutlet UIButton* btnBusiness5;
@property (nonatomic, retain) IBOutlet UIButton* btnBusiness6;

@property (nonatomic, weak) NSDictionary* dicBusiness;
-(IBAction)onOK:(id)sender;
-(IBAction)onBusiness:(id)sender;
@end
